package com.tj24.appmanager.model;

import android.app.Activity;
import com.tj24.base.utils.Sputil;
import com.tj24.appmanager.common.OrderConfig;

public class OrderModel extends BaseAppsManagerModel {
    public OrderModel(Activity mContext) {
        super(mContext);
    }

    /**
     * 获取排列方式
     * @return
     */
    public int getLayoutType(){
       return Sputil.read(OrderConfig.SP_LAYOUT_TYPE,OrderConfig.LAYOUT_LINEAR);
    }

    /**
     * 设置排序方式
     * @param layoutType
     */
    public void  setLayoutType(int layoutType){
        Sputil.save(OrderConfig.SP_LAYOUT_TYPE,layoutType);
    }
}
