package com.tj24.appmanager.common.CatFairyHeader;

import java.io.Serializable;

public class ModuleBean implements Serializable {
    private String name;
    private int picRes;

    public ModuleBean() {
    }

    public ModuleBean(String name, int picRes) {
        this.name = name;
        this.picRes = picRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicRes() {
        return picRes;
    }

    public void setPicRes(int picRes) {
        this.picRes = picRes;
    }
}
