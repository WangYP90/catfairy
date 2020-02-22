package com.tj24.wanandroid.common.event;

import com.tj24.base.base.event.BaseEvent;

public class TreeNaviRefreshEvent extends BaseEvent {
    /**
     * item  第几个fragment
     */
    private int item;

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public TreeNaviRefreshEvent(int item) {
        this.item = item;
    }

    /**
     * 发送刷新事件
     * @param item
     */
    public static void postRefresh(int item){
        new TreeNaviRefreshEvent(item).post();
    }
}
