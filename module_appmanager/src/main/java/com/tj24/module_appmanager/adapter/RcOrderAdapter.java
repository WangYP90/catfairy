package com.tj24.module_appmanager.adapter;

import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.module_appmanager.R;
import com.tj24.module_appmanager.view.dialog.OrderDialog;

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
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDialog.OrderHolder item) {
        helper.setText(R.id.tv_name,item.getOrderName())
                .setVisible(R.id.iv_select,item.isSelected());
    }
}
