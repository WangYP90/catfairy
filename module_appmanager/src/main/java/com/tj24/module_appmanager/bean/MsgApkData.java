package com.tj24.module_appmanager.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.io.Serializable;

public class MsgApkData extends SectionEntity<MsgApk>  implements Serializable {


    public MsgApkData(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MsgApkData(MsgApk msgApk) {
        super(msgApk);
    }

}
