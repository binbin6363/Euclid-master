package com.yalantis.euclid.net;

import android.os.Bundle;

/**
 * Created by bbwang on 2016/8/12.
 */
public abstract class TPPacket{

    public static int LOW_NET_PACKET_LEN = 512;                // 512Bit
    public static int MIDNET_PACKET_LEN = (4 * 1024);          // 4Kb
    public static int MAX_NET_PACKET_LEN = (4 * 1024 * 1024);  // 4Mb

    private static int transId = 0;

    public synchronized int getTransId() {
        return ++transId;
    }

    public byte[] encode() {
        // TODO: 不同的数据包，有不一样的编码方式，这个只是基类
        return new byte[0];
    }

    public int make(){
        return -1;
    }
}
