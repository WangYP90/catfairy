package com.tj24.base.base.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.stetho.Stetho;
import com.tj24.base.constant.Const;
import com.tj24.base.greendao.daohelper.GreenDaoManager;
import com.tj24.base.utils.CrashHandler;

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
        //初始化bmob
        initBmob();
        //调试
        initStetho();
    }

    private void initStetho() {
        if(!isDebug){
            return;
        }
        Stetho.initialize(Stetho
                .newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this)).build());
    }

    private void initBmob() {
        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId(Const.BMOB_APPLICATION_ID)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
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
