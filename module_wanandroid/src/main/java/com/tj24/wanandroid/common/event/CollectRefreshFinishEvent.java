package com.tj24.wanandroid.common.event;

import com.tj24.base.base.event.BaseEvent;

public class CollectRefreshFinishEvent extends BaseEvent {
    /**
     * 刷新的类型  0 refresh   1 loadmore
     */
    private int type;

    /**
     * 是否还有更多数据
     */
    private boolean haveMoreData;

    public static void  postRefreshFinishEvent(){
        new CollectRefreshFinishEvent(0,false).post();
    }
    public static void  postLoadMoreFinishEvent(boolean haveMoreData){
        new CollectRefreshFinishEvent(1,haveMoreData).post();
    }

    public CollectRefreshFinishEvent(int type, boolean haveMoreData) {
        this.type = type;
        this.haveMoreData = haveMoreData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHaveMoreData() {
        return haveMoreData;
    }

    public void setHaveMoreData(boolean haveMoreData) {
        this.haveMoreData = haveMoreData;
    }
}
