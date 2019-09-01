package com.tj24.appmanager.util.appsSort;

import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.appmanager.common.OrderConfig;

import java.util.Collections;
import java.util.List;

public class AppSortManager {
    public static void sort(List<AppBean> appBeans,String sortName){
        switch (sortName){
            case OrderConfig.ORDER_LAST_USE:
                Collections.sort(appBeans,new ComparatoLastOpen());
                break;
            case OrderConfig.ORDER_OPEN_NUM:
                Collections.sort(appBeans,new ComparatoOpenNum());
                break;
            case OrderConfig.ORDER_INSTALL_TIME:
                Collections.sort(appBeans,new ComparatorFirstInstal());
                break;
            case OrderConfig.ORDER_APP_NAME:
                Collections.sort(appBeans,new PinyinComparator());
                break;
            case OrderConfig.ORDER_USE_TIME:
                Collections.sort(appBeans,new ComparatoUserTime());
                break;
            case OrderConfig.ORDER_CUSTOM_PRIORITY:
                Collections.sort(appBeans,new ComparatoPriority());
                break;
        }
    }
}
