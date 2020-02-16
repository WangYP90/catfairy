package com.tj24.wanandroid.module.wx;

import android.view.View;

import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;

import java.util.List;

public class WxFragment extends BaseWanAndroidFragment {

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_wx;
    }

    @Override
    public void init(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        WxRequest.getWxs(new WanAndroidCallBack<List<TreeBean>>() {
            @Override
            public void onSucces(List<TreeBean> treeBeans) {
                treeBeans.size();
            }

            @Override
            public void onFail(String fail) {

            }
        });
    }
}
