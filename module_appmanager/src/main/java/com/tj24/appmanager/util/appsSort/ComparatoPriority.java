package com.tj24.appmanager.util.appsSort;

import com.tj24.base.bean.appmanager.AppBean;

/**
 * Created by energy on 2018/1/18.
 */

public class ComparatoPriority extends BaseAppsComparator {

    @Override
    public int compare(AppBean t0, AppBean t1) {
        if(t0.getPriority()<t1.getPriority()){
            return 1;
        }else if(t0.getPriority()==t1.getPriority()){
            return 0;
        }else {
            return -1;
        }
    }
}
