package com.tj24.module_appmanager.activity;

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
import butterknife.BindView;
import butterknife.OnClick;
import com.tj24.library_base.base.ui.BaseActivity;
import com.tj24.library_base.utils.ListUtil;
import com.tj24.module_appmanager.R;
import com.tj24.module_appmanager.adapter.SearchAdapter;
import com.tj24.module_appmanager.bean.AppBean;
import com.tj24.module_appmanager.greendao.daohelper.AppBeanDaoHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.scrim)
    View scrim;
    @BindView(R.id.fl_searchBackground)
    View flSearchBackground;
    @BindView(R.id.iv_searchBack)
    ImageView ivSearchBack;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.fl_searchToolbar)
    FrameLayout flSearchToolbar;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.noContentView)
    ViewStub noContentView;
    @BindView(R.id.loadErrorView)
    ViewStub loadErrorView;
    @BindView(R.id.fl_resultsContainer)
    FrameLayout flResultsContainer;
    @BindView(R.id.container)
    FrameLayout container;

    View mSearchButton;
    SearchView.SearchAutoComplete mSearchAutoComplete;

    private SearchAdapter searchAdapter;
    private LinearLayoutManager linearLayoutManager;
    private final List<AppBean> appBeans = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading.setVisibility(View.GONE);
        initSearchView();
        initRecyclerView();
        initTransition();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }


    private void initSearchView() {
        mSearchButton = findViewById(R.id.search_button);
        mSearchAutoComplete = searchView.findViewById(R.id.search_src_text);
        //设置搜索框有字时显示叉叉，无字时隐藏叉叉
        searchView.onActionViewExpanded();
        searchView.setIconified(true);

        searchView.setQueryHint("搜索APP名称");
        searchView.setIconified(false);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query)){
                    showShortToast("搜索内容不能为空！");
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
        searchAdapter = new SearchAdapter(R.layout.rc_search_item,appBeans);
        rvSearch.setLayoutManager(linearLayoutManager);
        rvSearch.setAdapter(searchAdapter);
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

    @OnClick({R.id.scrim, R.id.iv_searchBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scrim:
                dismiss();
                break;
            case R.id.iv_searchBack:
                dismiss();
                break;
        }
    }

    private void searchApp(String newText) {
        appBeans.clear();
        appBeans.addAll(AppBeanDaoHelper.getInstance().queryByWords(newText));
        searchAdapter.notifyDataSetChanged();
        searchAdapter.removeAllFooterView();
        if(!ListUtil.isNullOrEmpty(appBeans)){
            searchAdapter.addFooterView(getLayoutInflater().inflate(R.layout.search_footer_view,null));
        }
    }

    private void dismiss() {
        ivSearchBack.setBackground(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

}
