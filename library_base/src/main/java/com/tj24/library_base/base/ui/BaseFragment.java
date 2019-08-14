package com.tj24.library_base.base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.*;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.tj24.library_base.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
     * Fragment中由于服务器异常导致加载失败显示的布局。
     */
    private View erroView;

    /**
     * Fragment中由于网络异常导致加载失败显示的布局。
     */
    private View badNetWorkView;

    /**
     * Fragment中当界面上没有任何内容时展示的布局。
     */
    private View noContentView;

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
            if (layoutResId > 0)

                rootView = inflater.inflate(getCreateViewLayoutId(), container, false);
            // 解决点击穿透问题
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            unbinder = ButterKnife.bind(this, rootView);
        }
        init(rootView);
        EventBus.getDefault().register(this);
        return rootView;
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
     * 当Fragment中的加载内容服务器返回失败，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    public void showLoadErrorView(String tip) {
        if (erroView != null) {
            erroView.setVisibility(View.VISIBLE);
            return;
        }
        if (rootView != null) {
            ViewStub viewStub = rootView.findViewById(R.id.loadErrorView);
            if (viewStub != null) {
                erroView = viewStub.inflate();
                TextView loadErrorText = erroView.findViewById(R.id.loadErrorText);
                loadErrorText.setText(tip);
            }
        }
    }

    /**
     * 当Fragment中的内容因为网络原因无法显示的时候，通过此方法显示提示界面给用户。
     *
     * @param listener
     * 重新加载点击事件回调让
     */
    public void showBadNetworkView(View.OnClickListener listener) {
        if (badNetWorkView != null) {
            badNetWorkView.setVisibility(View.VISIBLE);
            return;
        }
        if (rootView != null) {
            ViewStub viewStub = rootView.findViewById(R.id.badNetworkView);
            if (viewStub != null) {
                badNetWorkView = viewStub.inflate();
                View badNetworkRootView = badNetWorkView.findViewById(R.id.badNetworkRootView);
                badNetworkRootView.setOnClickListener(listener);
            }
        }
    }

    /**
     * 当Fragment中没有任何内容的时候，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    public void showNoContentView(String tip) {
        if (noContentView != null) {
            noContentView.setVisibility(View.VISIBLE);
            return;
        }
        if (rootView != null) {
            ViewStub viewStub = rootView.findViewById(R.id.noContentView);
            if (viewStub != null) {
                noContentView = viewStub.inflate();
                TextView noContentText = noContentView.findViewById(R.id.noContentText);
                        noContentText.setText(tip);
            }
        }
    }

    /**
     * 当Fragment中没有任何内容的时候，通过此方法显示提示界面给用户。
     * @param tip
     * 界面中的提示信息
     * @param buttonText
     * 界面中的按钮的文字
     * @param listener
     * 按钮的点击事件回调
     */
    public void showNoContentViewWithButton(String tip, String buttonText, View.OnClickListener listener) {
        if (noContentView != null) {
            noContentView.setVisibility(View.VISIBLE);
            return;
        }
        if (rootView != null) {
            ViewStub viewStub = rootView.findViewById(R.id.noContentViewWithButton);
            if (viewStub != null) {
                noContentView = viewStub.inflate();
                TextView noContentText = noContentView.findViewById(R.id.noContentText);
                        Button  noContentButton = noContentView.findViewById(R.id.noContentButton);
                        noContentText.setText(tip);
                noContentButton.setText(buttonText);
                noContentButton.setOnClickListener(listener);
            }
        }
    }

    /**
     * 将load error view进行隐藏。
     */
    public void hideLoadErrorView() {
        erroView.setVisibility(View.GONE);
    }

    /**
     * 将no content view进行隐藏。
     */
    public void hideNoContentView() {
        noContentView.setVisibility(View.GONE);
    }

    /**
     * 将bad network view进行隐藏。
     */
    public void hideBadNetworkView() {
       badNetWorkView.setVisibility(View.GONE);
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

    /**
     * 开始加载，将加载等待控件显示。
     */
    @CallSuper
   public void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        hideBadNetworkView();
        hideNoContentView();
        hideLoadErrorView();
    }

    /**
     * 加载完成，将加载等待控件隐藏。
     */
    @CallSuper
   public void loadFinished() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 加载失败，将加载等待控件隐藏。
     */
    @CallSuper
   public void loadFailed(String msg) {
        progressBar.setVisibility(View.GONE);
    }
}
