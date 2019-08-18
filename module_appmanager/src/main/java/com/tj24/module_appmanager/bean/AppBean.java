package com.tj24.module_appmanager.bean;

import com.tj24.module_appmanager.greendao.base.AppsTypeConverter;
import org.greenrobot.greendao.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:app
 * @Createdtime:2019/3/21 22:28
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
@Entity
public class AppBean implements Serializable {
    private static final long serialVersionUID = 12L;
    @Id
    /**
     * 包名
     */
    private String packageName;
    /**
     * app名
     */
    private String name;
    /**
     * 图标
     */
    private String ico;
    /**
     * 首次安装时间
     */
    private long firstIntalTime;
    /**
     * 最后更新时间
     */
    private long lastUpdateTime;
    /**
     * 版本号
     */
    private  int versionCode;
    /**
     * 版本名称
     */
    private String versionName;
    /**
     * apk路径
     */
    private String apkSourceDir;
    /**
     * 对应的字母
     */
    private String letters;
    /**
     * 是否是系统自带
     */
    private boolean isSystemApp;
    /**
     * 是否能打开
     */
    private boolean isCanOpen;
    /**
     * 最后打开时间
     */
    private long lastOpenTime;
    /**
     * 处于前台时间
     */
    private long topProcessTime;
    /**
     * 打开次数
     */
    private int openNum;
    /**
     * 所属分类
     */
    @Convert(columnType = String.class, converter = AppsTypeConverter.class)
    private List<String> type;
    /**
     * 是否被选中
     */
    @Transient
    private boolean isSelected;
    /**
     * 优先级
     */
    private int priority;
    /**
     * 描述
     */
    private String describe;
    /**
     * 大小
     */
    private long size;
    @Generated(hash = 496554026)
    public AppBean(String packageName, String name, String ico, long firstIntalTime,
            long lastUpdateTime, int versionCode, String versionName,
            String apkSourceDir, String letters, boolean isSystemApp,
            boolean isCanOpen, long lastOpenTime, long topProcessTime, int openNum,
            List<String> type, int priority, String describe, long size) {
        this.packageName = packageName;
        this.name = name;
        this.ico = ico;
        this.firstIntalTime = firstIntalTime;
        this.lastUpdateTime = lastUpdateTime;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.apkSourceDir = apkSourceDir;
        this.letters = letters;
        this.isSystemApp = isSystemApp;
        this.isCanOpen = isCanOpen;
        this.lastOpenTime = lastOpenTime;
        this.topProcessTime = topProcessTime;
        this.openNum = openNum;
        this.type = type;
        this.priority = priority;
        this.describe = describe;
        this.size = size;
    }
    @Generated(hash = 285800313)
    public AppBean() {
    }
    public String getPackageName() {
        return this.packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIco() {
        return this.ico;
    }
    public void setIco(String ico) {
        this.ico = ico;
    }
    public long getFirstIntalTime() {
        return this.firstIntalTime;
    }
    public void setFirstIntalTime(long firstIntalTime) {
        this.firstIntalTime = firstIntalTime;
    }
    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    public int getVersionCode() {
        return this.versionCode;
    }
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
    public String getVersionName() {
        return this.versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    public String getApkSourceDir() {
        return this.apkSourceDir;
    }
    public void setApkSourceDir(String apkSourceDir) {
        this.apkSourceDir = apkSourceDir;
    }
    public String getLetters() {
        return this.letters;
    }
    public void setLetters(String letters) {
        this.letters = letters;
    }
    public boolean getIsSystemApp() {
        return this.isSystemApp;
    }
    public void setIsSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }
    public boolean getIsCanOpen() {
        return this.isCanOpen;
    }
    public void setIsCanOpen(boolean isCanOpen) {
        this.isCanOpen = isCanOpen;
    }
    public long getLastOpenTime() {
        return this.lastOpenTime;
    }
    public void setLastOpenTime(long lastOpenTime) {
        this.lastOpenTime = lastOpenTime;
    }
    public long getTopProcessTime() {
        return this.topProcessTime;
    }
    public void setTopProcessTime(long topProcessTime) {
        this.topProcessTime = topProcessTime;
    }
    public int getOpenNum() {
        return this.openNum;
    }
    public void setOpenNum(int openNum) {
        this.openNum = openNum;
    }
    public List<String> getType() {
        return this.type;
    }
    public void setType(List<String> type) {
        this.type = type;
    }
    public boolean getIsSelected() {
        return this.isSelected;
    }
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public int getPriority() {
        return this.priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public String getDescribe() {
        return this.describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
   
}
