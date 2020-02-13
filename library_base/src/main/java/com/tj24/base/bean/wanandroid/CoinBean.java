package com.tj24.base.bean.wanandroid;

import java.io.Serializable;

/**
 * @Description:积分
 * @Createdtime:2020/2/13 19:08
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class CoinBean implements Serializable {

    private int coinCount;
    private int level;
    private int rank;
    private int userId;
    private String username;

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
