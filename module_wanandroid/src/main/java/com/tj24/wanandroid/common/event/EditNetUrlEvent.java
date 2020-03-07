package com.tj24.wanandroid.common.event;

import com.tj24.base.base.event.BaseEvent;
import com.tj24.base.bean.wanandroid.NetUrlBean;

public class EditNetUrlEvent extends BaseEvent {
    //0 新增， 1 编辑
    private int type;
    private NetUrlBean netUrlBean;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NetUrlBean getNetUrlBean() {
        return netUrlBean;
    }

    public void setNetUrlBean(NetUrlBean netUrlBean) {
        this.netUrlBean = netUrlBean;
    }

    public EditNetUrlEvent(int type, NetUrlBean netUrlBean) {
        this.type = type;
        this.netUrlBean = netUrlBean;
    }

    public static void postEditNeturlEvent(int type, NetUrlBean netUrlBean){
        new EditNetUrlEvent(type,netUrlBean).post();
    }
}
