package com.tj24.wanandroid.module.mine.collect;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tj24.base.constant.ARouterPath;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.event.CollectRefreshEvent;
import com.tj24.wanandroid.common.event.CollectRefreshFinishEvent;
import com.tj24.wanandroid.user.LoginInterceptorCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;

@Route(path = ARouterPath.WanAndroid.COLLECT_ACTIVITY, extras = ARouterPath.WanAndroid.NEED_LOGIN)
public class CollectActivity extends BaseWanAndroidActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R2.id.rb_article)
    RadioButton rbArticle;
    @BindView(R2.id.rb_link)
    RadioButton rbLink;
    @BindView(R2.id.rg_collect)
    RadioGroup rgCollect;
    @BindView(R2.id.viewpager)
    ViewPager2 viewpager;
    @BindView(R2.id.refresh)
    SmartRefreshLayout refresh;

    CollectAdapter collectAdapter;
    public View transView;

    CollectLinkListener collectLinkListener;

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_collect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTransView();

        refresh.setEnableRefresh(true);
        refresh.setOnRefreshListener(this);
        collectAdapter = new CollectAdapter(this);
        viewpager.setAdapter(collectAdapter);
        rgCollect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbArticle.getId()) {
                    viewpager.setCurrentItem(0);
                } else if (checkedId == rbLink.getId()) {
                    viewpager.setCurrentItem(1);
                }
            }
        });

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 0){
                    rgCollect.check(rbArticle.getId());
                    refresh.setEnableLoadMore(true);
                    refresh.setOnLoadMoreListener(CollectActivity.this);
                }else {
                    rgCollect.check(rbLink.getId());
                    refresh.setEnableLoadMore(false);
                    refresh.setOnLoadMoreListener(null);
                }

                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        CollectRefreshEvent.postRefreshEvent(viewpager.getCurrentItem());
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        CollectRefreshEvent.postLoadMoreEvent(viewpager.getCurrentItem());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveFinishRefresh(CollectRefreshFinishEvent event){
        if (refresh.isRefreshing()) {
            refresh.finishRefresh();
        }
        if (refresh.isLoading()) {
            if (event.isHaveMoreData()) {
                refresh.finishLoadMore();
            } else {
                refresh.finishLoadMoreWithNoMoreData();
                refresh.setNoMoreData(false);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wanandroid_toolbar_collect_link, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_collect_link) {
            CollectLinkActivity.actionStart(this,CollectLinkActivity.TYPE_CREAT,null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_collect_link).setVisible(viewpager.getCurrentItem()==1);
        return super.onPrepareOptionsMenu(menu);
    }

    public void setCollectLinkListener(CollectLinkListener collectLinkListener){
        this.collectLinkListener = collectLinkListener;
    }

    float clickX;
    float clickY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickX = event.getX();
                clickY = event.getY();
                if(collectLinkListener != null){
                    collectLinkListener.onActivityClick(clickX,clickY);
                }
        }
        return super.dispatchTouchEvent(event);
    }

    //只放行点击事件，其他事件全部拦截
    private void initTransView() {
        transView = findViewById(R.id.trans_view);
        transView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collectLinkListener != null){
                    collectLinkListener.onTranseViewClick();
                }
            }
        });
        transView.setOnTouchListener(new View.OnTouchListener() {
            float x = 0;
            float y = 0;
            long time = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        time = System.currentTimeMillis();
                        return false;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(x - event.getX()) < 2 && Math.abs(y - event.getY()) < 2 && System.currentTimeMillis() - time < 300) {
                            return false;
                        } else {
                            return true;
                        }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(collectLinkListener != null){
            collectLinkListener.onBackPressed();
        }else {
           onBack();
        }
    }

    public void onBack(){
        super.onBackPressed();
    }

    public static void actionStart(Context context) {
        ARouter.getInstance().build(ARouterPath.WanAndroid.COLLECT_ACTIVITY).navigation(context, new LoginInterceptorCallBack(context));
    }

    class CollectAdapter extends FragmentStateAdapter {
        public CollectAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new CollectArticleFragment();
            } else if (position == 1) {
                return new CollectLinkFragment();
            } else {
                return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
