package com.tj24.base.bean.appmanager;

import org.greenrobot.greendao.annotation.*;

import java.io.Serializable;

/**
 * @Description:app分类
 * @Createdtime:2019/3/21 22:56
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
@Entity
public class AppClassfication implements Serializable {
    private static final long serialVersionUID = 11L;
    @Id@Unique
    /**
     * 类型主键
     */
    private String id;
    /**
     * 类型名称
     */
    private String name;

    /**
     * 对应的排序方式名称
     */
    private String sortName;

    /**
     * 是否是默认（系统应用，我的应用）
     */
    private boolean isDefault;
    /**
     * 顺序
     */
    private int order;
    @Generated(hash = 974152211)
    public AppClassfication(String id, String name, String sortName,
            boolean isDefault, int order) {
        this.id = id;
        this.name = name;
        this.sortName = sortName;
        this.isDefault = isDefault;
        this.order = order;
    }
    @Generated(hash = 1023070321)
    public AppClassfication() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortName() {
        return this.sortName;
    }
    public void setSortName(String sortName) {
        this.sortName = sortName;
    }
    public boolean getIsDefault() {
        return this.isDefault;
    }
    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    public int getOrder() {
        return this.order;
    }
    public void setOrder(int order) {
        this.order = order;
    }

}
