package com.tj24.base.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * @Description: 上传克隆 数据实体
 * @Createdtime:2019/10/20 20:21
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class PushRecord extends BmobObject implements Serializable {
    private String userId;
    private String userName;
    private String tag;
    private String dataId;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
