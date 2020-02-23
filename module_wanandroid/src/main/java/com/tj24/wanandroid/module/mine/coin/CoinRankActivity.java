package com.tj24.wanandroid.module.mine.coin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tj24.wanandroid.R;
import com.tj24.wanandroid.common.base.BaseWanAndroidActivity;

public class CoinRankActivity extends BaseWanAndroidActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.wanandroid_activity_coin;
    }

    public static void actionStart(Context context){
        Intent i = new Intent(context, CoinRankActivity.class);
        context.startActivity(i);
    }
}
