package com.tj24.module_appmanager.bean.event;

public class LaucherEvent {
    public static final int EVENT_START_EDITING = 1; //开始编辑
    public static final int EVENT_EXIST_EDITING = 2; //退出编辑
    public static final int EVENT_CHANGE_ORDERTYPE = 3; //排列方式改变 linealayout gridelayout
    public static final int EVENT_CHANGE_SORT = 4; //排序方式改变

    private int eventCode;
    private Object value;

    public LaucherEvent() {
    }

    public LaucherEvent(int eventCode) {
        this.eventCode = eventCode;
    }

    public LaucherEvent(int eventCode, Object value) {
        this.eventCode = eventCode;
        this.value = value;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
