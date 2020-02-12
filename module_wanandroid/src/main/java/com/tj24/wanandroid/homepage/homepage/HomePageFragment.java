package com.tj24.wanandroid.homepage.homepage;

import android.view.View;

import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.http.RetrofitWanAndroid;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.base.bean.wanandroid.homepage.BannerBean;

import java.util.List;

public class HomePageFragment extends BaseWanAndroidFragment {

    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_home_page;
    }

    @Override
    public void init(View view) {
        view.findViewById(R.id.tv);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitWanAndroid.getInstance().getApiClient().getBanners().enqueue(new WanAndroidCallBack<List<BannerBean>>() {
                    @Override
                    public void onSucces(List<BannerBean> bannerData) {

                    }

                    @Override
                    public void onFail(String fail) {

                    }
                });
            }
        });
    }

}
