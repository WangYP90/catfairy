package com.tj24.appmanager.bean;

import cn.bmob.v3.BmobObject;

/**
 * @Description:用户协议
 * @Createdtime:2019/10/20 12:38
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class UserAgrement extends BmobObject {
    /**
     * id
     */
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否正在使用
     */
    private boolean isUsing;
    /**
     * 创建时间
     */
    private long creatTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isUsing() {
        return isUsing;
    }

    public void setUsing(boolean using) {
        isUsing = using;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }
}
