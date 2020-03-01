package com.tj24.wanandroid.module.treenavigation.knowledge;

import com.tj24.base.bean.wanandroid.TreeBean;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class KnowledgeArticleAdapter extends FragmentStateAdapter {

    private TreeBean treeBean;

    public KnowledgeArticleAdapter(@NonNull FragmentActivity fragmentActivity,TreeBean treeBean) {
        super(fragmentActivity);
        this.treeBean = treeBean;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new KnowledgeArticleFragment(treeBean.getChildren().get(position));
    }


    @Override
    public int getItemCount() {
        return treeBean.getChildren().size();
    }
}
