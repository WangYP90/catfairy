package com.tj24.appmanager.common;

import androidx.collection.ArraySet;

import com.tj24.appmanager.R;
import com.tj24.base.base.app.BaseApplication;
import com.tj24.base.utils.Sputil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:排序的相关初始化
 * @Createdtime:2019/7/9 22:26
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class OrderConfig {

    public static final String ORDER_LAST_USE = "按最近使用排序";
    public static final String ORDER_OPEN_NUM = "按打开次数排序";
    public static final String ORDER_INSTALL_TIME = "按安装时间排序";
    public static final String ORDER_APP_NAME = "按应用名称排序";
    public static final String ORDER_USE_TIME = "按使用时长排序";
    public static final String ORDER_CUSTOM_PRIORITY = "自定义排序";

    public static final String ORDER_LAST_USE_KEY = "1";
    public static final String ORDER_OPEN_NUM_KEY = "2";
    public static final String ORDER_INSTALL_TIME_KEY = "3";
    public static final String ORDER_APP_NAME_KEY = "4";
    public static final String ORDER_USE_TIME_KEY = "5";
    public static final String ORDER_CUSTOM_PRIORITY_KEY = "6";

    public static final CharSequence[] orderKeyAll = new CharSequence[]{
            ORDER_LAST_USE_KEY,
            ORDER_OPEN_NUM_KEY,
            ORDER_INSTALL_TIME_KEY,
            ORDER_APP_NAME_KEY,
            ORDER_USE_TIME_KEY,
            ORDER_CUSTOM_PRIORITY_KEY
    };
    public static final CharSequence[] ordersAll = new CharSequence[]{
            ORDER_LAST_USE,
            ORDER_OPEN_NUM,
            ORDER_INSTALL_TIME,
            ORDER_APP_NAME,
            ORDER_USE_TIME,
            ORDER_CUSTOM_PRIORITY
    };
    public static final CharSequence[] orderKeyWithoutUseTime = new CharSequence[]{
            ORDER_LAST_USE_KEY,
            ORDER_OPEN_NUM_KEY,
            ORDER_INSTALL_TIME_KEY,
            ORDER_APP_NAME_KEY,
            ORDER_CUSTOM_PRIORITY_KEY
    };
    public static final CharSequence[] ordersWithoutUseTime = new CharSequence[]{
            ORDER_LAST_USE,
            ORDER_OPEN_NUM,
            ORDER_INSTALL_TIME,
            ORDER_APP_NAME,
            ORDER_CUSTOM_PRIORITY
    };

    public static Map<String,String> getOrderMap(){
        Map<String,String> orderMap = new HashMap<>();
        orderMap.put(ORDER_LAST_USE_KEY,ORDER_LAST_USE);
        orderMap.put(ORDER_OPEN_NUM_KEY,ORDER_OPEN_NUM);
        orderMap.put(ORDER_INSTALL_TIME_KEY,ORDER_INSTALL_TIME);
        orderMap.put(ORDER_APP_NAME_KEY,ORDER_APP_NAME);
        orderMap.put(ORDER_USE_TIME_KEY,ORDER_USE_TIME);
        orderMap.put(ORDER_CUSTOM_PRIORITY_KEY,ORDER_CUSTOM_PRIORITY);
        return orderMap;
    }
    /**
     * 获取设置的排序方式
     * @return
     */
    public static List<String> getOrderValues(){
        List<String> orderValues = new ArrayList<>();
        Map<String,String> orderMap = getOrderMap();
        Set<String> sets =  Sputil.read(BaseApplication.getContext().getString(R.string.app_sp_custom_order),new ArraySet<>());
        for(String set :sets){
            if(orderMap.get(set)!=null){
                orderValues.add(orderMap.get(set));
            }
        }
        return orderValues;
     }

    /**
     * 排列方式 存储key
     */
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
