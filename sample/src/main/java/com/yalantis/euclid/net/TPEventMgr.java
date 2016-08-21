package com.yalantis.euclid.net;

import com.yalantis.euclid.sample.TPContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by bbwang on 2016/8/12.
 */
public class TPEventMgr {

    private static TPEventMgr inst = new TPEventMgr();
    private ConcurrentLinkedQueue<TPEvent> recv_data_queue = new ConcurrentLinkedQueue<TPEvent>();
    private ConcurrentLinkedQueue<TPEvent> send_data_queue = new ConcurrentLinkedQueue<TPEvent>();

    private Map<Long, TPContext> buffered_context = new LinkedHashMap<Long, TPContext>();

    public static TPEventMgr Instance(){
        return inst;
    }

    private long makeId(int transId){
        long timeNow = System.currentTimeMillis()/1000; // get second
        return (timeNow << 32 | transId);
    }

    public int sendRequest(TPPacket packet){
        TPEvent event = new TPEvent();
        //event.setId(makeId(packet.getTransId()));
        event.setId(packet.getTransId());
        event.setTpPacket(packet);
        send_data_queue.add(event);
        return 0;
    }

    public int recvData(TPPacket packet){
        TPEvent event = new TPEvent();
        event.setId(packet.getTransId());
        event.setTpPacket(packet);
        recv_data_queue.add(event);
        return 0;
    }

    public TPEvent getRecvEvent(){
        return recv_data_queue.poll();
    }

    public TPEvent getSendEvent() {
        return send_data_queue.poll();
    }

    public synchronized int Register(TPEvent sendEvent) {
        buffered_context.put(sendEvent.getId(), sendEvent.getContext());
        return 0;
    }

    public synchronized int Unregister(TPEvent sendEvent) {
        if (buffered_context.containsKey(sendEvent.getId())) {
            buffered_context.remove(sendEvent.getId());
        } else {
            System.err.println("buffered context not exist id "+sendEvent.getId());
        }
        return 0;
    }
}
