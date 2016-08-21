package com.yalantis.euclid.net;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.yalantis.euclid.global.GlobalConfig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SocketService extends IntentService {

    private static boolean isStopNetWorker = false;
    private static Socket serverSocekt;
    private static InetSocketAddress remoteAddr;
    private static InputStream socketInputSatream;
    private static OutputStream socketOutputSatream;
    private static int DEFAULT_BUF_LEN = 1;
    private static int TMP_BUF_LEN = (1024*1024*4); // 4Mb
    private static ByteBuffer readTmpBuffer = ByteBuffer.allocate(DEFAULT_BUF_LEN); // later 1 => 4Mb
    private int readIndex = 0;             // 当前读到的位置
    private int packetStart = 0;           // 包的起始位置
    private static String TAG = "SocketService";
    private TPHandler handler;

    // socket工作线程
    private static SocketWorkerRunnable socketWorkerRunnable = null;
    private static Thread socketWorkerThread = null;

    // socket连接线程
    private static SocketConnectorRunnable socketConnectorRunnable = null;
    private static Thread socketConnectorThread = null;

    // socket连接成功与否
    private static final Object netConnectDone = new Object();

    // 收到的一个完整包
    private static TPPacket packet = null;

    public SocketService() {
        super("SocketService");
        if (socketWorkerRunnable == null) {
            socketWorkerRunnable = new SocketWorkerRunnable();
        }
        if (socketWorkerThread == null) {
            socketWorkerThread = new Thread(socketWorkerRunnable);
        }

        if (socketConnectorRunnable == null) {
            socketConnectorRunnable = new SocketConnectorRunnable();
        }

        if (socketConnectorThread == null) {
            socketConnectorThread = new Thread(socketConnectorRunnable);
        }
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SocketService.class);
        //intent.setAction(ACTION_FOO);
        //intent.putExtra(EXTRA_PARAM1, param1);
        //intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    // 先初始化配置再调用这个接口
    public static int InitSocket(){
        if (GlobalConfig.IS_PRODUCT_ENV) {
            remoteAddr = new InetSocketAddress(GlobalConfig.PRODUCT_SERVER_IP, GlobalConfig.PRODUCT_SERVER_PORT);
            Log.i(TAG, "init socket in product environment, addr: " + remoteAddr.toString());
        } else {
            remoteAddr = new InetSocketAddress(GlobalConfig.DEV_SERVER_IP, GlobalConfig.DEV_SERVER_PORT);
            Log.i(TAG, "init socket in development environment, addr: " + remoteAddr.toString());
        }

        return 0;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        // 启动网络连接
        Log.d(TAG, "onStart, connect server " + remoteAddr.toString());

        /*
        try {
            netConnectDone.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        // todo:
        // socketConnectorThread.start();

        Log.d(TAG, "done onStart");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "onHandleIntent, wait for moment connecting server, then start socket service.");

        // 启动socket工作线程，循环处理网络事件
        //try {
            // TODO: 假设这个wait先执行，等待connect ok之后通知这里
            //synchronized (netConnectDone) {
                // TODO: 2016/8/18
                // netConnectDone.wait();
           // }
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        Log.d(TAG, "onHandleIntent, connect server succeed, start socket service.");
        if (!socketWorkerThread.isAlive()) {
            Log.i(TAG, "start socket worker thread.");
            // todo:
            // socketWorkerThread.start();
        }
        Log.d(TAG, "done onHandleIntent");
    }

    // 读取socket数据，返回值含义如下：
    // 0，标示需要继续读取
    // 1，标示有一个完整的包
    // -1，标示暂时没有数据，需要下次再读
    // -2，标示socket被对端关闭
    private int readSocket(){
        int ret = 0;
        try {
            for (; ; ) {
                int readN = socketInputSatream.read(readTmpBuffer.array(), readIndex, TMP_BUF_LEN - readIndex);
                if (readN == -1) {
                    Log.e(TAG, "done read data, wait next read ...");
                    // 分包逻辑
                    ret = -1;
                    break;
                } else if (readN == 0) {
                    Log.e(TAG, "read EOF! socket close by peer?");
                    ret = -2;
                    break;
                }
                readIndex += readN;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    // 返回收到数据包的个数
    private int doRecvData() {
        int MAX_READ_TRY_COUNT = 10; // 尝试循环读取socket数据的次数
        int packetNum = 1;
        if (serverSocekt.isInputShutdown()) {
            Log.e(TAG, "socket input is shutdown! socket is closed. server addr: " +remoteAddr.toString() );
            return -1;
        }

        if (readTmpBuffer.limit() == 1) {
            readTmpBuffer = ByteBuffer.allocateDirect(TMP_BUF_LEN);
        }

        int tryCount = 0;
        for (packetNum = readSocket(); packetNum == 0; packetNum = readSocket()) {
            ++tryCount;
            if (tryCount > MAX_READ_TRY_COUNT) {
                Log.e(TAG, "read socket try count more than " + MAX_READ_TRY_COUNT);
                break;
            }
        }

        return packetNum;
    }

    // 发送数据到socket
    private int doSendData(TPEvent sendEvent) {
        if (serverSocekt.isOutputShutdown()){
            Log.e(TAG, "socket output is shutdown! send failed. server addr: "+remoteAddr.toString());
            return -1;
        }
        if (sendEvent.getId() != 0) {
            TPEventMgr.Instance().Register(sendEvent);
        }

        TPPacket packet = sendEvent.getTpPacket();
        if (packet == null) {
            Log.e(TAG, "send failed, packet is null. server addr: "+remoteAddr.toString());
            return -2;
        }
        byte[] data = packet.encode();
        try {
            if (data == null || data.length == 0) {
                Log.e(TAG, "send failed, encode data failed. server addr: "+remoteAddr.toString());
                return -3;
            }
            socketOutputSatream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "send failed. IOException, server addr: "+remoteAddr.toString());
            return -4;
        }
        Log.d(TAG, "send data success, data len:" + data.length);
        return 0;
    }

    // ------------------------------------------------------------------------------------------ //
    // socket connect runnable
    private class SocketConnectorRunnable implements Runnable {

        @Override
        public void run() {
            Log.i(TAG, "start connect net., thread:"+ Thread.currentThread().getName());
            try {
                // 耗时操作
                serverSocekt = new Socket();
                serverSocekt.connect(remoteAddr, GlobalConfig.NET_CONNECT_TIMEOUT);
                serverSocekt.setReuseAddress(true);
                socketInputSatream = new DataInputStream(serverSocekt.getInputStream());
                socketOutputSatream = new DataOutputStream(serverSocekt.getOutputStream());

                Log.i(TAG, "connect succeed, notify start socket worker thread, my thread:"+ Thread.currentThread().getName());
                synchronized (netConnectDone) {
                    netConnectDone.notify();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "connect to server failed, addr: " + remoteAddr.toString());
            }
        }
    }

    // socket work runnable
    private class SocketWorkerRunnable implements Runnable {

        @Override
        public void run() {
            boolean haveEvent = false;
            Log.i(TAG, "after notify, start socket worker thread, my thread:"+ Thread.currentThread().getName());

            for (; ;) {

                if (!isStopNetWorker) {
                    break;
                }

                if (serverSocekt.isClosed()) {
                    try {
                        Thread.sleep(GlobalConfig.NET_RECONNECT_INTERNAL);
                        serverSocekt.connect(remoteAddr, GlobalConfig.NET_CONNECT_TIMEOUT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                if (!serverSocekt.isConnected()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                // 从队列取发送事件，送到socket
                TPEvent sendEvent = TPEventMgr.Instance().getSendEvent();
                if (sendEvent != null) {
                    doSendData(sendEvent);
                    haveEvent = true;
                }

                // 从socket取接受到的数据，解析,通过handler通知给逻辑层
                if (1 == doRecvData()) {
                    Message message = new Message();
                    message.what = TPHandler.TYPE_DATA_RECV;
                    // 将解析的packet设法告诉逻辑层，处理后更新ui
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "data_recved");
                    message.setData(bundle);

                    handler.sendMessage(message);
                    haveEvent = true;
                }

                if (!haveEvent) {
                    try {
                        haveEvent = false;
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
