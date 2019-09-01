package com.tj24.base.base.ui;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:activity的收集器
 * @Createdtime:2019/3/10 19:56
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class ActivityCollector {

    private static final String TAG = ActivityCollector.class.getSimpleName();

    private static List<WeakReference<Activity>> activityList = new ArrayList<>();

    public static int getSize(){
        return activityList.size();
    }

   public static void add(WeakReference<Activity> weakRefActivity) {
        activityList.add(weakRefActivity);
    }

    public static void remove(WeakReference<Activity> weakRefActivity) {
        boolean result = activityList.remove(weakRefActivity);
        Log.d(TAG, "remove activity reference $result");
    }

    public static void  finishAll() {
        if (!activityList.isEmpty()) {
            for (WeakReference<Activity> weakReference : activityList) {
                Activity activity = weakReference.get();
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
            }
            activityList.clear();
        }
    }
}
