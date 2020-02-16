package com.tj24.wanandroid.module.homepage;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.common.base.CommonArticleFragment;
import com.tj24.wanandroid.common.event.HomePageRefreshEvent;
import com.tj24.wanandroid.common.event.HomePageRefreshFinishEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomePageProjectFragment extends CommonArticleFragment {

    @Override
    public boolean isCanRefresh() {
        return false;
    }

    @Override
    public void initData(int page) {
        HomePageRequest.requestProject(page, new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                HomePageRefreshFinishEvent.postLoadMoreFinishEvent(articleRespon.getPageCount()>=page);
                pageNum++;
                datas.addAll(articleRespon.getDatas());
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String fail) {
                HomePageRefreshFinishEvent.postLoadMoreFinishEvent(false);
                ToastUtil.showShortToast(mActivity,fail);
            }
        });
    }

    @Override
    public void refreshData() {
        HomePageRequest.requestProjectByNet(getFirstPage(), new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                HomePageRefreshFinishEvent.postRefreshFinishEvent();
                pageNum = getFirstPage()+1;
                datas.clear();
                datas.addAll(articleRespon.getDatas());
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String fail) {
                HomePageRefreshFinishEvent.postRefreshFinishEvent();
                ToastUtil.showShortToast(mActivity,fail);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveRefresh(HomePageRefreshEvent event){
        if(event.getItem() == 1){
            if(event.getType()==0){
                refreshData();
            }else if(event.getType()==1){
                initData(pageNum);
            }
        }
    }

    @Override
    public int getFirstPage() {
        return 0;
    }
}
