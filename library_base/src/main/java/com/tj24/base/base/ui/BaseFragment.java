package com.tj24.base.base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tj24.base.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Description:fragment基类
 * @Createdtime:2019/3/10 23:40
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 所属activity
     */
    public Activity mActivity;

    private  PermissionListener mListener = null;

    /**
     * Fragment中inflate出来的布局。
     */
    protected View rootView;

    /**
     * Fragment中显示加载等待的控件。
     */
    protected  ProgressBar progressBar;

    Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    /**
     * 返回Fragment设置ContentView的布局文件资源ID
     */
    public abstract int getCreateViewLayoutId();

    /**
     * 此方法用于初始化
     */
    public abstract void init(View view);

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            int layoutResId = getCreateViewLayoutId();
            if (layoutResId > 0){
                rootView = inflater.inflate(getCreateViewLayoutId(), container, false);
            }
            // 解决点击穿透问题
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            unbinder = ButterKnife.bind(this, rootView);
        }

        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(rootView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message messageEvent) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 检查和处理运行时权限，并将用户授权的结果通过PermissionListener进行回调。
     *
     * @param permissions
     * 要检查和处理的运行时权限数组
     * @param listener
     * 用于接收授权结果的监听器
     */
    public void handlePermissions(ArrayList<String> permissions, PermissionListener listener) {
        if (permissions == null || mActivity == null) {
            return;
        }
        mListener = listener;
        List<String> requestPermissionList = new ArrayList<String>();
        for (int i = 0 ;i<permissions.size();i++) {
            if (ContextCompat.checkSelfPermission(mActivity, permissions.get(i)) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permissions.get(i));
            }
        }
        if (!requestPermissionList.isEmpty()) {
            requestPermissions((String[]) requestPermissionList.toArray(), 1);
        } else {
            listener.onGranted();
        }
    }

    public void  onRequestPermissionsResult( int requestCode, String [] permissions, int [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1 :
                if(grantResults!=null && grantResults.length>0){
                    List<String> deniedPermissions = new ArrayList<>();
                    for(int i= 0 ;i <grantResults.length; i ++){
                        int  grantResult = grantResults[i];
                        String permission = permissions[i];
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            deniedPermissions.add(permission);
                        }
                    }
                    if(deniedPermissions.isEmpty()){
                        mListener.onGranted();
                    }else {
                        mListener.onDenied(deniedPermissions);
                    }
                }
        }
    }

    /**
     * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
     * @param view
     * Fragment中inflate出来的View实例。
     * @return  Fragment中inflate出来的View实例原封不动返回。
     */
    public View  onCreateView(View view){
        rootView = view;
        progressBar = view.findViewById(R.id.loading);
        return view;
    }

}
