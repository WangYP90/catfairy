package com.tj24.base.bean.wanandroid;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HistoryKey {
    @Id
    private String name;
    private long searchTime;
    @Generated(hash = 142672088)
    public HistoryKey(String name, long searchTime) {
        this.name = name;
        this.searchTime = searchTime;
    }
    @Generated(hash = 668397952)
    public HistoryKey() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getSearchTime() {
        return this.searchTime;
    }
    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }
}
