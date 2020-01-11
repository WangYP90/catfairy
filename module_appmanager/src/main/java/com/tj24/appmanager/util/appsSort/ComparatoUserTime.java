package com.tj24.appmanager.util.appsSort;

import com.tj24.base.bean.appmanager.AppBean;

/**
 * Created by energy on 2018/1/18.
 */

public class ComparatoUserTime extends BaseAppsComparator {

    @Override
    public int compare(AppBean myAppInfo, AppBean t1) {
        if(myAppInfo.getTopProcessTime()<t1.getTopProcessTime()){
            return 1;
        }else if(myAppInfo.getTopProcessTime()==t1.getTopProcessTime()){
            return myAppInfo.getName().compareTo(t1.getName());
        }else {
            return -1;
        }
    }
}
