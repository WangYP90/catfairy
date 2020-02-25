package com.tj24.wanandroid.module.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

public class SearchActivity extends BaseWanAndroidActivity {

    SearchView mSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_search;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wanandroid_toolbar_search, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setMaxWidth(500);
        mSearchView.setQueryHint("输入搜索内容");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_coin_rule) {
            //           "https://www.wanandroid.com/blog/show/2653"
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,SearchActivity.class);
        context.startActivity(intent);
    }
}
