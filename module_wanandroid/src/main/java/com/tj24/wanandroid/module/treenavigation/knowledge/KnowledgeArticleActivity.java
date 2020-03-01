package com.tj24.wanandroid.module.treenavigation.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tj24.base.bean.wanandroid.TreeBean;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;

public class KnowledgeArticleActivity extends BaseWanAndroidActivity {

    private static String EXT_TREEBEAN = "treebean";
    private static String EXT_POSITION = "position";

    @BindView(R2.id.tablayout)
    TabLayout tablayout;
    @BindView(R2.id.viewpager)
    ViewPager2 viewpager;

    TreeBean treeBean;
    int position;

    private KnowledgeArticleAdapter knowledgeArticleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        treeBean = (TreeBean) getIntent().getSerializableExtra(EXT_TREEBEAN);
        position = getIntent().getIntExtra(EXT_POSITION,0);

        toolbar.setTitle(treeBean.getName());
        knowledgeArticleAdapter = new KnowledgeArticleAdapter(this,treeBean);
        viewpager.setAdapter(knowledgeArticleAdapter);
        viewpager.setCurrentItem(position);
        linkTabViewPager();
    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_knowledge_article;
    }

    private void linkTabViewPager() {
        TabLayoutMediator tabLayoutMediator=  new TabLayoutMediator(tablayout, viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(treeBean.getChildren().get(position).getName());
            }
        });
        tabLayoutMediator.attach();
    }


    public static void actionStart(Context context, TreeBean treeBean, int position){
        Intent intent = new Intent(context,KnowledgeArticleActivity.class);
        intent.putExtra(EXT_TREEBEAN,treeBean);
        intent.putExtra(EXT_POSITION,position);
        context.startActivity(intent);
    }

}
