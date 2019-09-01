package com.tj24.base.base.ui;

import java.util.List;

/**
 * @Description:权限申请接口
 * @Createdtime:2019/3/10 19:52
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
