package com.tj24.wanandroid.module.treenavigation;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.TreeNaviRefreshEvent;
import com.tj24.wanandroid.common.event.TreeNaviRefreshFinishEvent;
import com.tj24.wanandroid.module.treenavigation.knowledge.KnowledgeFragment;
import com.tj24.wanandroid.module.treenavigation.navi.NaviFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;

public class TreeNaviFragment extends BaseWanAndroidFragment implements OnRefreshListener {

    @BindView(R2.id.rb_knowledge)
    RadioButton rbKnowledge;
    @BindView(R2.id.rb_navi)
    RadioButton rbNavi;
    @BindView(R2.id.rg_homePage)
    RadioGroup rg;
    @BindView(R2.id.viewpager)
    ViewPager2 viewpager;
    @BindView(R2.id.refresh)
    SmartRefreshLayout refresh;
    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_tree_navi;
    }

    @Override
    public void init(View view) {
        refresh.setEnableRefresh(true);
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(this);

        viewpager.setAdapter( new TreeNaviAdapter(this));
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbKnowledge.getId()) {
                    viewpager.setCurrentItem(0);
                } else if (checkedId == rbNavi.getId()) {
                    viewpager.setCurrentItem(1);
                }
            }
        });

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 0){
                    rg.check(rbKnowledge.getId());
                }else {
                    rg.check(rbNavi.getId());
                }
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        TreeNaviRefreshEvent.postRefresh(viewpager.getCurrentItem());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveFinishRefresh(TreeNaviRefreshFinishEvent event){
        refresh.finishRefresh();
    }


    class TreeNaviAdapter extends FragmentStateAdapter {

        public TreeNaviAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new KnowledgeFragment();
            } else if (position == 1) {
                return new NaviFragment();
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
