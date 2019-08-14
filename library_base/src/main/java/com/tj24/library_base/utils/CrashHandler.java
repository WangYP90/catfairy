package com.tj24.library_base.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.tj24.library_base.base.app.BaseApplication;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @Description:崩溃日志收集类
 * @Createdtime:2019/3/3 0:19
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    private static final String PATH = "sdcard/";
    private static final String FILE_NAME = "tj24";

    //log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".txt";

    private static CrashHandler sInstance = new CrashHandler();

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;

    //构造方法私有，防止外部构造多个实例，即采用单例模式
    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    //这里主要完成初始化工作
    public void init(Context context) {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //获取Context，方便内部使用
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        //导出异常信息到SD卡中
        String path = dumpExceptionToSDCard(ex);
        //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
        uploadExceptionToServer(path);
        Toast.makeText(BaseApplication.getContext(),"程序出现异常，即将退出",Toast.LENGTH_LONG).show();
        //打印出当前调用栈信息
        ex.printStackTrace();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                    handleCrash(thread, ex);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void handleCrash(Thread thread, Throwable ex) {
        //如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private String dumpExceptionToSDCard(Throwable ex) {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        String path = "";
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted,skip dump exception");
            }
        } else {
            File dir = new File(PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            long current = System.currentTimeMillis();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
            //以当前时间创建log文件
            File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);

            try {
                StringWriter writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                StringBuffer sb = new StringBuffer();
                //导出发生异常的时间
                pw.println(time);

                //导出手机信息
                dumpPhoneInfo(pw, sb);

                pw.println();
                //导出异常的调用栈信息
                ex.printStackTrace(pw);
                pw.close();

                sb.append(writer.toString());
                path = sb.toString();
            } catch (Exception e) {
                Log.e(TAG, "dump crash info failed");
            }
        }
        return path;
    }


    private void dumpPhoneInfo(PrintWriter pw, StringBuffer sb) throws PackageManager.NameNotFoundException {
        //应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        sb.append("_");
        pw.println(pi.versionCode);


        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    /**
     * 将异常信息进行上传
     * @param sb
     */
    private void uploadExceptionToServer(final String sb) {

    }
}