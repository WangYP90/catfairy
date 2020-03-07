package com.tj24.wanandroid.common.event;

import com.tj24.base.base.event.BaseEvent;

public class CollectChangeEvent extends BaseEvent {
    //文章id
    private int id;
    //变为后的状态
    private boolean isCollected;

    public CollectChangeEvent() {
    }

    public CollectChangeEvent(int id, boolean isCollected) {
        this.id = id;
        this.isCollected = isCollected;
    }

    public static void postCollectChangeEvent(int id,boolean isCollected){
        new CollectChangeEvent(id,isCollected).post();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }
}
