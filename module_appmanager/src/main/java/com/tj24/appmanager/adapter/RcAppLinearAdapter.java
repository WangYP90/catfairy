package com.tj24.appmanager.adapter;

import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj24.base.utils.DateUtil;
import com.tj24.appmanager.R;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.appmanager.common.OrderConfig;

import java.util.List;

/**
 * @Description:app列表的线性adapter
 * @Createdtime:2019/3/24 14:33
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class RcAppLinearAdapter extends BaseQuickAdapter<AppBean,BaseViewHolder> {
    private boolean isEditing;
    private AppClassfication appClassfication;
    public RcAppLinearAdapter(int layoutResId, @Nullable List<AppBean> data,AppClassfication appClassfication,boolean isEditing) {
        super(layoutResId, data);
        this.isEditing = isEditing;
        this.appClassfication = appClassfication;
    }

    @Override
    protected void convert(BaseViewHolder helper, AppBean item) {
        ImageView ivIco = helper.getView(R.id.iv_ico);
        Glide.with(ivIco.getContext()).load(item.getIco()).into(ivIco);
        helper.setText(R.id.tv_name,item.getName())
                .setText(R.id.tv_show,getShowText(item))
                .setVisible(R.id.iv_isType,!isTyped(item))
                .setGone(R.id.tv_open,!isEditing)
                .setGone(R.id.iv_selected,isEditing)
                .setImageResource(R.id.iv_selected,item.getIsSelected()?R.drawable.app_ico_app_selected :R.drawable.app_ico_app_unselected)
                .addOnClickListener(R.id.tv_open);
    }

    /**
     * 动态改变要展示的内容
     * @param item
     * @return
     */
    private String getShowText(AppBean item) {
        switch (appClassfication.getSortName()){
            case OrderConfig.ORDER_LAST_USE:
                return item.getLastOpenTime()!=0?OrderConfig.ORDER_LAST_USE+":"+DateUtil.formatLong(DateUtil.SDF_3,item.getLastOpenTime()):"未曾使用";
            case OrderConfig.ORDER_OPEN_NUM:
                return OrderConfig.ORDER_OPEN_NUM+":"+item.getOpenNum();
            case OrderConfig.ORDER_INSTALL_TIME:
                return OrderConfig.ORDER_INSTALL_TIME+":"+DateUtil.formatLong(DateUtil.SDF_3,item.getFirstIntalTime());
            case OrderConfig.ORDER_APP_NAME:
                return "包名:"+item.getPackageName();
            case OrderConfig.ORDER_USE_TIME:
                return OrderConfig.ORDER_USE_TIME+":"+ DateUtil.formatDuring(item.getTopProcessTime());
            case OrderConfig.ORDER_CUSTOM_PRIORITY:
                return OrderConfig.ORDER_CUSTOM_PRIORITY+":"+item.getPriority();
        }
        return "";
    }

    /**
     * 是否需要展示未分类标签
     * 1.系统APP不需要展示；
     * 2.非系统APP 已添加到自定义分类不需要展示；
     * @return
     */
    public boolean isTyped(AppBean data) {
        if(data.getIsSystemApp()){
            return true;
        }
        if(data.getType()!=null && data.getType().size()>1){
            return true;
        }
        return false;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
