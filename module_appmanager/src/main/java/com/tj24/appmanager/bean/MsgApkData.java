package com.tj24.appmanager.bean;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.tj24.base.bean.appmanager.MsgApk;

import java.io.Serializable;

/**
 * @Description:安装卸载记录页面 实体
 * @Createdtime:2019/9/1 23:19
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class MsgApkData extends SectionEntity<MsgApk>  implements Serializable {

    public MsgApkData(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MsgApkData(MsgApk msgApk) {
        super(msgApk);
    }

}
