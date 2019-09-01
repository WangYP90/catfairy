package com.tj24.appmanager.util.appsSort;

import com.tj24.base.bean.appmanager.AppBean;

/**
 * Created by energy on 2018/1/18.
 */

public class ComparatorFirstInstal extends BaseAppsComparator {

    @Override
    public int compare(AppBean myAppInfo, AppBean t1) {
        if(myAppInfo.getFirstIntalTime()<t1.getFirstIntalTime()){
            return 1;
        }else if(myAppInfo.getFirstIntalTime()==t1.getFirstIntalTime()){
            return 0;
        }else {
            return -1;
        }
    }
}
