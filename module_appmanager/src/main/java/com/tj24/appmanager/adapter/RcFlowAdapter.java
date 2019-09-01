package com.tj24.appmanager.adapter;

import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.base.common.recyclerview.itemTouchhelper.IDragAdapter;
import com.tj24.appmanager.R;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.appmanager.daohelper.AppClassificationDaoHelper;

import java.util.List;

/**
 * @Description:流式布局 appclassification adapter
 * @Createdtime:2019/8/1 21:53
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class RcFlowAdapter extends BaseQuickAdapter<AppClassfication,BaseViewHolder> implements IDragAdapter {
    public RcFlowAdapter(int layoutResId, @Nullable List<AppClassfication> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppClassfication item) {
        helper.setText(R.id.tv_classification_name,item.getName())
                .setGone(R.id.iv_del,!item.getIsDefault())
                .setBackgroundRes(R.id.classification_container,item.getIsDefault()?
                        R.drawable.app_shap_corner_edit_default_type:R.drawable.app_shap_corner_edit_type);
    }

    @Override
    public boolean onItemMoved(int position, int target) {
        if (target < 2 || position < 2) {
            return false;
        }
        notifyItemMoved(position, target);
        if (position > target) {
            mData.get(position).setOrder(target);
            for (int i = target; i < position; i++) {
                mData.get(i).setOrder(i+1);
            }
            AppClassificationDaoHelper.getInstance().getDao().updateInTx(mData);
        }
        if (position < target) {
            mData.get(position).setOrder(target);
            for (int i = target; i > position; i--) {
                mData.get(i).setOrder(i-1);
            }
            AppClassificationDaoHelper.getInstance().getDao().updateInTx(mData);
        }
        return true;
    }
}
