package com.tj24.wanandroid.common.event;

import com.tj24.base.base.event.BaseEvent;

public class LoginEvent extends BaseEvent {

    public static final int LOGIN_SUCCES = 8;
    public static final int LOGIN_OUT = 24;

    private int loginCode;

    public LoginEvent(int loginCode) {
        this.loginCode = loginCode;
    }

    public int getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(int loginCode) {
        this.loginCode = loginCode;
    }

    public static void postLoginSuccess(){
        new LoginEvent(LOGIN_SUCCES).post();
    }

    public static void postLoginOut(){
        new LoginEvent(LOGIN_OUT).post();
    }

}
