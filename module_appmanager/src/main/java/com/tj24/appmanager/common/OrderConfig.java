package com.tj24.appmanager.common;

import java.util.HashMap;
import java.util.Map;
/**
 * @Description:排序的相关初始化
 * @Createdtime:2019/7/9 22:26
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class OrderConfig {
    private static final Map<Integer,String> orderMap = new HashMap<>();

    public static final String ORDER_LAST_USE = "最近使用";
    public static final String ORDER_OPEN_NUM = "打开次数";
    public static final String ORDER_INSTALL_TIME = "安装时间";
    public static final String ORDER_APP_NAME = "应用名称";
    public static final String ORDER_USE_TIME = "使用时长";
    public static final String ORDER_CUSTOM_PRIORITY = "自定义优先级";

    public static final int ORDER_LAST_USE_ID = 1;
    public static final int ORDER_OPEN_NUM_ID = 2;
    public static final int ORDER_INSTALL_TIME_ID = 3;
    public static final int ORDER_APP_NAME_ID = 4;
    public static final int ORDER_USE_TIME_ID = 5;
    public static final int ORDER_CUSTOM_PRIORITY_ID = 6;

    public static Map<Integer,String> getOrderMap(){
        if(orderMap.size()<=0){
            orderMap.put(ORDER_LAST_USE_ID,ORDER_LAST_USE);
            orderMap.put(ORDER_OPEN_NUM_ID,ORDER_OPEN_NUM);
            orderMap.put(ORDER_INSTALL_TIME_ID,ORDER_INSTALL_TIME);
            orderMap.put(ORDER_APP_NAME_ID,ORDER_APP_NAME);
            orderMap.put(ORDER_USE_TIME_ID,ORDER_USE_TIME);
            orderMap.put(ORDER_CUSTOM_PRIORITY_ID,ORDER_CUSTOM_PRIORITY);
        }
        return orderMap;
    }

    public static final String SP_LAYOUT_TYPE = "sp_layout_type";

    /**
     * 线性排序
     */
    public static final int LAYOUT_LINEAR = 12;
    /**
     * gride排序
     */
    public static final int LAYOUT_Gride = 13;
}
