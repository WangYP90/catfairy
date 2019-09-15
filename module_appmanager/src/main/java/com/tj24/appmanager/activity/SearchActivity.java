package com.tj24.appmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.SharedElementCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tj24.appmanager.R;
import com.tj24.appmanager.adapter.SearchAdapter;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.appmanager.model.ApkModel;
import com.tj24.base.base.ui.BaseActivity;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private View scrim;
    private View flSearchBackground;
    private ImageView ivSearchBack;
    private SearchView searchView;
    private FrameLayout flSearchToolbar;
    private RecyclerView rvSearch;
    private ProgressBar loading;
    private ViewStub noContentView;
    private ViewStub loadErrorView;
    private FrameLayout flResultsContainer;
    private FrameLayout container;

    View mSearchButton;
    SearchView.SearchAutoComplete mSearchAutoComplete;

    private SearchAdapter searchAdapter;
    private LinearLayoutManager linearLayoutManager;
    private final List<AppBean> appBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initSearchView();
        initRecyclerView();
        initTransition();
    }


    @Override
    public int getLayoutId() {
        return R.layout.app_activity_search;
    }

    private void initView() {
        scrim = findViewById(R.id.scrim);
        flSearchBackground = findViewById(R.id.fl_searchBackground);
        ivSearchBack = findViewById(R.id.iv_searchBack);
        searchView = findViewById(R.id.searchView);
        flSearchToolbar = findViewById(R.id.fl_searchToolbar);
        rvSearch = findViewById(R.id.rv_search);
        loading = findViewById(R.id.loading);
        noContentView = findViewById(R.id.noContentView);
        loadErrorView = findViewById(R.id.loadErrorView);
        flResultsContainer = findViewById(R.id.fl_resultsContainer);
        container = findViewById(R.id.container);
        scrim.setOnClickListener(this);
        ivSearchBack.setOnClickListener(this);
        loading.setVisibility(View.GONE);
    }

    private void initSearchView() {
        mSearchButton = findViewById(R.id.search_button);
        mSearchAutoComplete = searchView.findViewById(R.id.search_src_text);
        //设置搜索框有字时显示叉叉，无字时隐藏叉叉
        searchView.onActionViewExpanded();
        searchView.setIconified(true);

        searchView.setQueryHint(getString(R.string.app_search_app_name));
        searchView.setIconified(false);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query)){
                    showShortToast(getString(R.string.app_search_content_not_null));
                }else {
                    searchApp(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchApp(newText);
                return false;
            }
        });
    }



    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        searchAdapter = new SearchAdapter(R.layout.app_rv_search_item,appBeans);
        rvSearch.setLayoutManager(linearLayoutManager);
        rvSearch.setAdapter(searchAdapter);
        searchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.tv_open){
                    ApkModel.openApp(mActivity,appBeans.get(position));
                }
            }
        });
    }


    private void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
//                    if(!ListUtil.isNullOrEmpty(sharedElements)){
//                        View searchIcon = sharedElements.get(0);
//                        if(searchIcon.getId() == R.id.iv_searchBack){
//                          int centerX = (searchIcon.getRight() + searchIcon.getLeft())/2;
////                            val hideResults = TransitionUtils.findTransition(window.returnTransition as TransitionSet, CircularReveal::class.java, R.id.resultsContainer) as CircularReveal?
////                            hideResults?.setCenter(Point(centerX, 0))
//                        }
//                    }
                }

                @Override
                public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                    mSearchButton.performClick();
                }
            });
        } else {
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                    mSearchButton.performClick();
                 }
             },100);
        }
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.scrim) {
            exsit();
        } else if (i == R.id.iv_searchBack) {
            exsit();
        }
    }

    private void exsit() {
        ivSearchBack.setBackground(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    /**
     * 搜寻查找
     * @param newText
     */
    private void searchApp(String newText) {
        appBeans.clear();
        appBeans.addAll(AppBeanDaoHelper.getInstance().queryByWords(newText));
        searchAdapter.notifyDataSetChanged();
        searchAdapter.removeAllFooterView();
        if(!ListUtil.isNullOrEmpty(appBeans)){
            searchAdapter.addFooterView(getLayoutInflater().inflate(R.layout.app_search_footer_view,null));
        }
    }

    public static void actionStart(Activity context){
        Intent i = new Intent(context,SearchActivity.class);
        context.startActivity(i);
    }

    public static void actionStartWithOptions(Activity context,Bundle options){
        Intent i = new Intent(context,SearchActivity.class);
        context.startActivity(i,options);
    }

}
