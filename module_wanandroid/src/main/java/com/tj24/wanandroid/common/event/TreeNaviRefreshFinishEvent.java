package com.tj24.wanandroid.common.event;

import com.tj24.base.base.event.BaseEvent;

public class TreeNaviRefreshFinishEvent extends BaseEvent {

    public static void postRefreshFinishEvent(){
        new TreeNaviRefreshFinishEvent().post();
    }
}
