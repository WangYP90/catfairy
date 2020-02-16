package com.tj24.wanandroid.common.http.cache;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jakewharton.disklrucache.DiskLruCache;
import com.tj24.base.constant.Const;
import com.tj24.base.utils.MD5Util;
import com.tj24.wanandroid.common.utils.WanSpUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description: wanAndroid 缓存数据
 * @Createdtime:2020/2/14 13:51
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class CacheWan {

    private static final int MAX_SIZE = 10 * 1024 * 1024;

    private static CacheWan INSTANCE;

    private DiskLruCache mDiskLruCache = null;
    private Gson mGson = new Gson();

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = new CacheWan();
        }
    }

    public static CacheWan getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("WanCache未初始化");
        }
        return INSTANCE;
    }

    private CacheWan() {
        openDiskLruCache();
    }

    private DiskLruCache getDiskLruCache() {
        if (mDiskLruCache == null) {
            throw new RuntimeException("WanCache未初始化或初始化失败");
        }
        return mDiskLruCache;
    }

    public void openDiskLruCache() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
            try {
                mDiskLruCache.close();
                File cacheDir = Const.CACHE_WAN_CACHE;
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }

                mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, MAX_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        public boolean isSame(Object cache, Object net) {
            String cacheJson = mGson.toJson(cache);
            String netJson = mGson.toJson(net);
            return TextUtils.equals(cacheJson, netJson);
        }

        public void save(String key, Object bean) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        saveSync(key, bean);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        public  void remove(String key, CacheListener cacheListener) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        removeSync(key);
                        cacheListener.onSuccess(key);
                    } catch (IOException e) {
                        e.printStackTrace();
                        cacheListener.onFailed();
                    }
                }
            }).start();
        }


        public void getCache(String key, CacheListener cacheListener) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (getDiskLruCache()) {
                        try {
                            DiskLruCache.Snapshot snapshot = getDiskLruCache().get(MD5Util.encode(key));
                            String json = snapshot.getString(0);
                            snapshot.close();
                            cacheListener.onSuccess(json);
                        } catch (IOException e) {
                            e.printStackTrace();
                            cacheListener.onFailed();
                        }

                    }
                }
            }).start();
        }

        private void removeSync(String key) throws IOException {
            synchronized (getDiskLruCache()) {
                getDiskLruCache().remove(MD5Util.encode(key));

                WanSpUtil.clear(key);
            }
        }

        private void saveSync(String key, Object bean) throws IOException {
            synchronized (getDiskLruCache()) {
                DiskLruCache.Editor editor = getDiskLruCache().edit(MD5Util.encode(key));
                editor.set(0, mGson.toJson(bean));
                editor.commit();
                getDiskLruCache().flush();

                WanSpUtil.save(key,System.currentTimeMillis());
            }
        }


        private <T> T getBeanSync(String key, Class<T> clazz) throws IOException {
            synchronized (getDiskLruCache()) {
                DiskLruCache.Snapshot snapshot = getDiskLruCache().get(MD5Util.encode(key));
                String json = snapshot.getString(0);
                T bean = mGson.fromJson(json, clazz);
                snapshot.close();
                return bean;
            }
        }

        private <T> List<T> getListSync(String key, Class<T> clazz) throws IOException {
            synchronized (getDiskLruCache()) {
                DiskLruCache.Snapshot snapshot = getDiskLruCache().get(MD5Util.encode(key));
                String json = snapshot.getString(0);
                List<T> list = jsonToBeanList(json, clazz);
                snapshot.close();
                return list;
            }
        }

        private <T> List<T> jsonToBeanList(String json, Class<T> t) {
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            List<T> list = new ArrayList<>();
            JsonParser parser = new JsonParser();
            JsonArray jsonarray = parser.parse(json).getAsJsonArray();
            for (JsonElement element : jsonarray) {
                list.add(mGson.fromJson(element, t));
            }
            return list;
        }

    }
