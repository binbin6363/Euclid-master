package com.yalantis.euclid.net;

import com.yalantis.euclid.sample.TPContext;

/**
 * Created by bbwang on 2016/8/12.
 */
public class TPEvent {
    private TPContext context;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TPPacket getTpPacket() {
        return tpPacket;
    }

    public void setTpPacket(TPPacket tpPacket) {
        this.tpPacket = tpPacket;
    }

    // 64位id
    private long id;
    private TPPacket tpPacket;

    public TPContext getContext() {
        return context;
    }
}
