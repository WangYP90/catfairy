package com.tj24.library_base.utils;

import java.util.List;

/**
 * @Description:集合相关操作类
 * @Createdtime:2019/3/10 21:18
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class ListUtil {

    /**
     * s是否为空或者为空
     * @param list
     * @return
     */
    public static boolean isNullOrEmpty(List list){
        return list == null || list.isEmpty();
    }
}
