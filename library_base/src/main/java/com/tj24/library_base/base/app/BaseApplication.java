package com.tj24.library_base.base.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.stetho.Stetho;
import com.tj24.library_base.greendao.daohelper.GreenDaoManager;
import com.tj24.library_base.utils.CrashHandler;

/**
 * @Description:基类application
 * @Createdtime:2019/3/2 22:34
 * @Author:TangJiang
 * @Version: V.1.0.0
 */

public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class.getSimpleName();
    private boolean isDebug = true;
    private static Context context;
    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
         Log.e(TAG,"开始初始化application!");
        context = this;
        mouduleApplicationInit();
        // 初始化闪崩异常捕捉
        CrashHandler.getInstance().init(context);
        //初始化ARouter
        initARouter();
        //初始化grenndao
        GreenDaoManager.init();

        initStetho();
    }

    private void initStetho() {
        Stetho.initialize(Stetho
                .newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this)).build());
    }



    private void initARouter() {
        if(isDebug){
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    /**
     * 通过反射初始化其他模块的application
     */
    private void mouduleApplicationInit(){
        for(String mouduleImpl:ApplicationList.APPLICITONLIST){
            try  {
                Class<?> clas = Class.forName(mouduleImpl);
                Object object = clas.newInstance();
                if(object instanceof IApplication){
                    ((IApplication) object).onCreat(this);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
