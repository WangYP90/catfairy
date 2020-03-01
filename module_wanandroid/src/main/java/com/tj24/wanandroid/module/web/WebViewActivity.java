package com.tj24.wanandroid.module.web;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.utils.UserUtil;
import com.tj24.wanandroid.module.mine.collect.CollectRequest;
import com.tj24.wanandroid.user.LoginActivity;

import butterknife.BindView;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

public class WebViewActivity extends BaseWanAndroidActivity {

    private static final String TITLE = "title";
    private static final String URL = "url";

    String title;
    String url;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra(TITLE);
        url = getIntent().getStringExtra(URL);

        initTitle();
        initWebView();
        webView.loadUrl(url);
    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_web_view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wanandroid_webview, menu);
        toolbar.setNavigationIcon(R.drawable.crop__ic_cancel);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_share){
            share();
        }else if(item.getItemId() == R.id.menu_refresh){
            webView.reload();
            progress.setVisibility(View.VISIBLE);
        }else if(item.getItemId() == R.id.menu_copy_link){
            copyLink();
        }else if(item.getItemId() == R.id.menu_open_chorome){
            //第三方浏览器打开
            Uri issuesUrl = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, issuesUrl);
            startActivity(intent);
        }else if(item.getItemId() == R.id.menu_collect){
            collect();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTitle() {
        toolbar.setTitle(title);
        progress.setVisibility(View.VISIBLE);
        progress.setProgress(0);
    }

    private void initWebView() {
        WebSettings ws = webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 不缩放
        webView.setInitialScale(100);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
        //        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

        //显示进度条
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //显示进度条
                progress.setProgress(newProgress);
                if (newProgress == 100) {
                    //加载完毕隐藏进度条
                    progress.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
            progress.setProgress(0);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 收藏此网址  注意：可以多次收藏
     */
    private void collect() {
        if(!UserUtil.getInstance().isLogin()){
            LoginActivity.actionStart(this);
        }
        CollectRequest.collectLink(title, url, new WanAndroidCallBack() {
            @Override
            public void onSucces(Object o) {
                showShortToast("收藏成功");
            }

            @Override
            public void onFail(String fail) {
                showShortToast("收藏失败");
            }
        });
    }

    private void copyLink() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", url);
        clipboard.setPrimaryClip(clip);
        showShortToast("链接已复制");
    }

    private void share() {
        String shareText = title + url;
        new Share2.Builder(this).setContentType(ShareContentType.TEXT)
                .setTextContent(shareText)
                .setTitle("Share File")
                .build()
                .shareBySystem();
    }

    public static void actionStart(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }
}
