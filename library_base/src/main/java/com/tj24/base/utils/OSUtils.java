package com.tj24.base.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 *  *用于判断手机操作系统类型的工具类。
 */
public class OSUtils {
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMiUI8OrLower(){
        try {
            BuildProperties  prop = BuildProperties.newInstance();
            boolean isMiUI = (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null);
            if (isMiUI) {
                String versionName = prop.getProperty(KEY_MIUI_VERSION_NAME, null);
                if (!TextUtils.isEmpty(versionName) && versionName.startsWith("V")) {
                    String versionNumber = versionName.replace("V", "");
                    int versionCode = Integer.parseInt(versionNumber);
                    if (versionCode < 9) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

private static class BuildProperties{
  private Properties properties;
  private boolean isEmpty;

    public static BuildProperties newInstance(){
        return new BuildProperties();
    }

    public BuildProperties() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            isEmpty = properties.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }

    public String getProperty(String name){
        return properties.getProperty(name);
    }

    public String getProperty(String name,String defaultValue){
        return properties.getProperty(name, defaultValue);
    }

    public Enumeration<Object> keys() {
        return properties.keys();
    }

    public Set keySet() {
        return properties.keySet();
    }

    public int  size(){
        return properties.size();
    }

    public Collection values(){
        return properties.values();
    }
}
}