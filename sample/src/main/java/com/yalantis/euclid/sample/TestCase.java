package com.yalantis.euclid.sample;

import android.util.Log;

import com.yalantis.euclid.global.GlobalConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Created by bbwang on 2016/8/13.
 */
public class TestCase {
    private static final String TAG = "TestCase";

    public static void main(String[] args) {
        runConnectServer();

        //runConnectServerChannel();
    }

    private static void runConnectServerChannel() {
        SocketAddress remoteAddr = new InetSocketAddress(GlobalConfig.PRODUCT_SERVER_IP, GlobalConfig.PRODUCT_SERVER_PORT);
        SocketChannel channel = null;
        try {
            System.out.println("start to connect server "+remoteAddr.toString());
            channel = SocketChannel.open();
            //channel.configureBlocking(false);
            channel.connect(remoteAddr);
            System.out.println("connect success.");


        } catch (IOException e) {
            System.out.println("connect failed.");
            e.printStackTrace();
        }
    }


    private static void runConnectServer(){
        SocketAddress remoteAddr = new InetSocketAddress(GlobalConfig.PRODUCT_SERVER_IP, GlobalConfig.PRODUCT_SERVER_PORT);
        //SocketAddress remoteAddr = new InetSocketAddress(inetAddress, GlobalConfig.PRODUCT_SERVER_PORT);
        try {
            Socket serverSocekt = new Socket(GlobalConfig.PRODUCT_SERVER_IP, GlobalConfig.PRODUCT_SERVER_PORT);
            // 耗时操作
            System.out.println("start to connect server "+remoteAddr.toString());
            //serverSocekt.connect(remoteAddr, 5000);
            serverSocekt.setReuseAddress(true);
            InputStream socketInputSatream = serverSocekt.getInputStream();
            OutputStream socketOutputSatream = serverSocekt.getOutputStream();
            System.out.println("connect success.");
            Thread.sleep(100000);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("connect failed.");
            Log.e(TAG, "connect to server failed, addr: " + remoteAddr.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

