package com.tj24.wanandroid.module.square;

import com.tj24.base.bean.wanandroid.ArticleBean;
import com.tj24.base.utils.ToastUtil;
import com.tj24.wanandroid.common.base.CommonArticleFragment;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.http.respon.ArticleRespon;

public class SquareFragment extends CommonArticleFragment {

    @Override
    public boolean isCanRefresh() {
        return true;
    }

    @Override
    public void initData(int page) {
        SquareRequest.requestSquareArticle(page, new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                finishRefresh(page>articleRespon.getPageCount());
                pageNum++;
                datas.addAll(articleRespon.getDatas());
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String fail) {
                finishRefresh(false);
                ToastUtil.showShortToast(mActivity,fail);
            }
        });
    }


    @Override
    public void refreshData() {
        SquareRequest.requestSquareArticleWitoutCache(getFirstPage(), new WanAndroidCallBack<ArticleRespon<ArticleBean>>() {
            @Override
            public void onSucces(ArticleRespon<ArticleBean> articleRespon) {
                finishRefresh(pageNum >articleRespon.getPageCount());
                pageNum = getFirstPage()+1;
                datas.clear();
                datas.addAll(articleRespon.getDatas());
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String fail) {
                finishRefresh(false);
                ToastUtil.showShortToast(mActivity,fail);
            }
        });
    }


    @Override
    public int getFirstPage() {
        return 0;
    }

}
