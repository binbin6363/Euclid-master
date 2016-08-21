package com.yalantis.euclid;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yalantis.euclid.net.SocketService;
import com.yalantis.euclid.packet.SyncTaskListPacket;
import com.yalantis.euclid.net.TPEvent;
import com.yalantis.euclid.net.TPEventMgr;
import com.yalantis.euclid.net.TPPacket;

/**
 * Created by bbwang on 2016/8/12.
 */
public class TPApp extends Application {

    private static final String TAG = "app";
    private static TPApp inst = null;
    public TPApp(){
        if (inst == null) {
            inst = this;
        }
    }

    public static Context getContext(){
        if (inst != null){
            return inst.getBaseContext();
        } else {
            return null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化配置

        // 初始化网络
        SocketService.InitSocket();

        // 启动socket服务连接服务器
        Intent socketIntent = new Intent(this.getApplicationContext(), SocketService.class);
        startService(socketIntent);

        startWorker();

    }

    private void startWorker() {
        // 1. 组包，获取任务列表
        makeSyncTaskList();
    }

    private void makeSyncTaskList() {
        TPPacket packet = new SyncTaskListPacket();
        packet.make();
        TPEvent event = new TPEvent();
        event.setTpPacket(packet);

        TPEventMgr.Instance().sendRequest(packet);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static String getSdCardPath(){
        //得到SD卡路径
        String DATABASE_PATH = android.os.Environment
                .getExternalStorageDirectory().getAbsolutePath();
        Log.i(TAG, "get sdcard path:"+DATABASE_PATH);
        return DATABASE_PATH;
    }
}
