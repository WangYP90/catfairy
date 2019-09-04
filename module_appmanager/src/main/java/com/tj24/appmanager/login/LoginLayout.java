package com.tj24.appmanager.login;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tj24.appmanager.R;

public class LoginLayout extends LinearLayout {

    boolean keyboardShowed = false;
    RelativeLayout rlLoginTop;
    LinearLayout loginWall;
    public boolean isKeyboardShowed() {
        return keyboardShowed;
    }

    public LoginLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed,left,top,right,bottom);
        rlLoginTop = findViewById(R.id.rl_login_top);
        loginWall = findViewById(R.id.ll_login_wall);
        if (changed) {
            int width = right - left;
            int height = bottom - top;
            if (height < 0.75*width) { // 如果高宽比小于4:3说明此时键盘弹出

                        loginWall.setVisibility(INVISIBLE);
                        LinearLayout.LayoutParams params = (LayoutParams) rlLoginTop.getLayoutParams();
                        params.weight = 1.5f;
                        keyboardShowed = true;
                        rlLoginTop.requestLayout();
                    }


            } else {

                        if (keyboardShowed) {
                            loginWall.setVisibility(INVISIBLE);
                            LayoutParams params = (LayoutParams) rlLoginTop.getLayoutParams();
                            params.weight = 6;
                            rlLoginTop.requestLayout();
                        }

        }
    }
}
