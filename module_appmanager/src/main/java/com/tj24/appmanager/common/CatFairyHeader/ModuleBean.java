package com.tj24.appmanager.common.CatFairyHeader;

import java.io.Serializable;

public class ModuleBean implements Serializable {
    private String name;
    private int picRes;
    private String aroutPath;
    public ModuleBean() {
    }

    public ModuleBean(String name, int picRes, String aroutPath) {
        this.name = name;
        this.picRes = picRes;
        this.aroutPath = aroutPath;
    }

    public String getAroutPath() {
        return aroutPath;
    }

    public void setAroutPath(String aroutPath) {
        this.aroutPath = aroutPath;
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
