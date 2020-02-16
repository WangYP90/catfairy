package com.tj24.wanandroid.common.event;

import com.tj24.base.base.event.BaseEvent;

public class HomePageRefreshEvent extends BaseEvent {
    /**
     * 刷新类型  0 下拉刷新    1上拉加载
     */
    private int type;
    /**
     * 刷新的页
     */
    private int item;


    public static void  postRefreshEvent(int item){
        new HomePageRefreshEvent(0,item).post();
    }

    public static void  postLoadMoreEvent(int item){
        new HomePageRefreshEvent(1,item).post();
    }

    public HomePageRefreshEvent(int type, int item) {
        this.type = type;
        this.item = item;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
