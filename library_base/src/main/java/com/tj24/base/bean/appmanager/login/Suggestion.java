package com.tj24.base.bean.appmanager.login;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * @Description:建议反馈
 * @Createdtime:2019/10/20 0:07
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
@Entity
public class Suggestion extends BmobObject implements Serializable {
    public static final long serialVersionUID  = 10l;
    @Id
    /**
     * ID
     */
    private String id;
    /**
     * 提交者 对应登录人ID
     */
    private String senderId;
    /**
     * 提交者 对应登录人名称
     */
    private String senderName;
    /**
     * 联系方式
     */
    private String concatInfo;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 创建时间
     */
    private long creatTime;
    @Generated(hash = 1265077824)
    public Suggestion(String id, String senderId, String senderName,
            String concatInfo, String content, long creatTime) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.concatInfo = concatInfo;
        this.content = content;
        this.creatTime = creatTime;
    }
    @Generated(hash = 998731485)
    public Suggestion() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSenderId() {
        return this.senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getSenderName() {
        return this.senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getConcatInfo() {
        return this.concatInfo;
    }
    public void setConcatInfo(String concatInfo) {
        this.concatInfo = concatInfo;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getCreatTime() {
        return this.creatTime;
    }
    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

}
