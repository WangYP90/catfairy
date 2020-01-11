package com.tj24.appmanager.adapter;

import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.appmanager.R;
import com.tj24.appmanager.common.OrderConfig;
import com.tj24.appmanager.view.dialog.OrderDialog;

import java.util.List;
/**
 * @Description:选择排序方式时 的 排序列表
 * @Createdtime:2019/7/31 21:54
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class RcOrderAdapter extends BaseQuickAdapter<OrderDialog.OrderHolder,BaseViewHolder> {

    public RcOrderAdapter(int layoutResId, @Nullable List<OrderDialog.OrderHolder> data) {
        super(layoutResId, data);
        if(data.isEmpty()){
            data.add(new OrderDialog.OrderHolder(OrderConfig.ORDER_APP_NAME,false));
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDialog.OrderHolder item) {
        helper.setText(R.id.tv_name,item.getOrderName())
                .setVisible(R.id.iv_select,item.isSelected());
    }
}
