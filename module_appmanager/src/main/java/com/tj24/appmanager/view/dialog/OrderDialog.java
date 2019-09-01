package com.tj24.appmanager.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj24.base.utils.ScreenUtil;
import com.tj24.appmanager.R;
import com.tj24.appmanager.adapter.RcOrderAdapter;
import com.tj24.base.bean.appmanager.AppClassfication;
import com.tj24.appmanager.bean.event.LaucherEvent;
import com.tj24.appmanager.common.OrderConfig;
import com.tj24.appmanager.daohelper.AppClassificationDaoHelper;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * @Description:排序的dialog
 * @Createdtime:2019/8/1 21:06
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public class OrderDialog extends Dialog implements BaseQuickAdapter.OnItemClickListener,DialogInterface.OnDismissListener{

    private LinearLayoutManager layoutManager;
    private RcOrderAdapter mAdapter;
    private AppClassfication appClassfication;
    private Context mContext;
    private RecyclerView rcOrder;
    private TextView tvShadow;
    private List<OrderHolder> orders = new ArrayList<>();
    private View view;
    public OrderDialog(@NonNull Context context,AppClassfication appClassfication,TextView tvShadow) {
        super(context,R.style.transparentDialog);
        this.mContext = context;
        this.appClassfication = appClassfication;
        this.tvShadow = tvShadow;
        init();
    }

    protected void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.apps_dialog_order, null);
        rcOrder = view.findViewById(R.id.rc_order);
        setOnDismissListener(this);
        setCanceledOnTouchOutside(true);

        //根据所选类别tab，显示其对应的排序dialog
        for(String order : OrderConfig.getOrderMap().values()){
            orders.add(new OrderHolder(order,order.equals(appClassfication.getSortName())));
        }
        layoutManager = new LinearLayoutManager(mContext);
        rcOrder.setLayoutManager(layoutManager);
        mAdapter = new RcOrderAdapter(R.layout.rc_order_dialog,orders);
        rcOrder.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void setPosition() {
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.y = ScreenUtil.getActionBarHeight(mContext)*2;
        //控件底部的坐标减去状态栏的高度
        dialogWindow.setBackgroundDrawableResource(R.color.white_text);
        dialogWindow.setAttributes(params);
    }


    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        tvShadow.setAlpha(0);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        for (OrderHolder order:orders){
            order.setSelected(false);
        }
        orders.get(position).setSelected(true);
        appClassfication.setSortName(orders.get(position).getOrderName());
        AppClassificationDaoHelper.getInstance().insertObj(appClassfication);
        mAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new LaucherEvent(LaucherEvent.EVENT_CHANGE_SORT,orders.get(position).getOrderName()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },200);
    }

    @Override
    public void show() {
        super.show();
        setContentView(view);
        setPosition();
        tvShadow.setAlpha(0.5f);
    }

    public static class OrderHolder{
        private String orderName;
        private boolean isSelected;

        public OrderHolder() {
        }

        public OrderHolder(String orderName, boolean isSelected) {
            this.orderName = orderName;
            this.isSelected = isSelected;
        }

        public String getOrderName() {
            return orderName;
        }

        public void setOrderName(String orderName) {
            this.orderName = orderName;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
