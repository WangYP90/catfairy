package com.tj24.base.base.event;

import org.greenrobot.eventbus.EventBus;

public class BaseEvent {
    public void post(){
        EventBus.getDefault().post(this);
    }
    
    public void postSticky(){
        EventBus.getDefault().post(this);
    }
}
