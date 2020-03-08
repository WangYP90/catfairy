package com.tj24.wanandroid.module;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.tj24.base.constant.ARouterPath;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.event.LoginEvent;
import com.tj24.wanandroid.module.homepage.HomePageFragment;
import com.tj24.wanandroid.module.mine.collect.CollectActivity;
import com.tj24.wanandroid.module.project.ProjectsFragment;
import com.tj24.wanandroid.module.search.SearchActivity;
import com.tj24.wanandroid.module.square.SquareFragment;
import com.tj24.wanandroid.module.treenavigation.TreeNaviFragment;
import com.tj24.wanandroid.module.wx.WxFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.OnClick;

@Route(path = ARouterPath.WanAndroid.MAIN_ACTIVITY)
public class HomePageActivity extends BaseWanAndroidActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.fragment_container)
    LinearLayout fragmentContainer;
    @BindView(R2.id.appBar)
    AppBarLayout appBar;
    @BindView(R2.id.nav_view)
    NavigationView navView;
    @BindView(R2.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    @BindView(R2.id.fbt_compose)
    FloatingActionButton fbtCompose;
    @BindView(R2.id.drawerLayout)
    DrawerLayout drawerLayout;

    NavigationHelper navigationHelper;
    Fragment homePageFragment;
    Fragment squareFragment;
    Fragment subscriptionFragment;
    Fragment systemNavigationFragment;
    Fragment projectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_home_page;
    }

    private void initView() {
        navigationHelper = new NavigationHelper(drawerLayout,navView,this);

        showFragment(0);

        drawerLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                navView.getViewTreeObserver().removeOnPreDrawListener(this);
                 navigationHelper.loadUserInfo();
                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wanandroid_toolbar_to_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            SearchActivity.actionStart(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_home) {
            showFragment(0);
        } else if (item.getItemId() == R.id.navigation_square) {
            showFragment(1);
        } else if (item.getItemId() == R.id.navigation_subscription) {
            showFragment(2);
        } else if (item.getItemId() == R.id.navigation_system) {
            showFragment(3);
        } else if (item.getItemId() == R.id.navigation_project) {
            showFragment(4);
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveLoginEvent(LoginEvent event){
        if(navigationHelper !=null){
            navigationHelper.loadUserInfo();
        }
    }

    public void showFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);
        if (index == 0) {
            if (homePageFragment == null) {
                homePageFragment = new HomePageFragment();
                ft.add(R.id.fragment_container, homePageFragment);
            } else {
                ft.show(homePageFragment);
            }
        } else if (index == 1) {
            if (squareFragment == null) {
                squareFragment = new SquareFragment();
                ft.add(R.id.fragment_container, squareFragment);
            } else {
                ft.show(squareFragment);
            }
        } else if (index == 2) {
            if (subscriptionFragment == null) {
                subscriptionFragment = new WxFragment();
                ft.add(R.id.fragment_container, subscriptionFragment);
            } else {
                ft.show(subscriptionFragment);
            }
        } else if (index == 3) {
            if (systemNavigationFragment == null) {
                systemNavigationFragment = new TreeNaviFragment();
                ft.add(R.id.fragment_container, systemNavigationFragment);
            } else {
                ft.show(systemNavigationFragment);
            }
        } else if (index == 4) {
            if (projectFragment == null) {
                projectFragment = new ProjectsFragment();
                ft.add(R.id.fragment_container, projectFragment);
            } else {
                ft.show(projectFragment);
            }
        }
        ft.commitAllowingStateLoss();
    }

    public void hideFragment(FragmentTransaction ft) {
        //如果不为空，就先隐藏起来
        if (homePageFragment != null) {
            ft.hide(homePageFragment);
        }
        if (squareFragment != null) {
            ft.hide(squareFragment);
        }
        if (subscriptionFragment != null) {
            ft.hide(subscriptionFragment);
        }
        if (systemNavigationFragment != null) {
            ft.hide(systemNavigationFragment);
        }
        if (projectFragment != null) {
            ft.hide(projectFragment);
        }
    }

    @OnClick(R2.id.fbt_compose)
    public void onClick() {
        startActivity(new Intent(this, CollectActivity.class));
    }
}
