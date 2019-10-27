package com.tj24.appmanager.daohelper;

import android.text.TextUtils;

import com.tj24.appmanager.R;
import com.tj24.base.base.app.BaseApplication;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.greendao.AppBeanDao;
import com.tj24.base.greendao.daohelper.BaseDao;
import com.tj24.base.utils.ListUtil;
import com.tj24.base.utils.PinyinUtils;
import com.tj24.base.utils.Sputil;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

public class AppBeanDaoHelper extends BaseDao<AppBean, AppBeanDao> {
    public static AppBeanDaoHelper daoHelper;
    public static AppBeanDaoHelper getInstance(){
        if(daoHelper == null){
            synchronized (AppBeanDaoHelper.class){
                if(daoHelper ==null){
                    daoHelper = new AppBeanDaoHelper();
                }
            }
        }
        return daoHelper;
    }

    @Override
    public AbstractDao getDao() {
        return mDaosession.getAppBeanDao();
    }

    /**
     * 根据classfication ID 查找app
     * @param id
     * @return
     */
    public List<AppBean> queryAppByClassficationId(String id){
        List<AppBean> appBeans = new ArrayList<>();
        for(AppBean appBean : queryAll()){
            if(!ListUtil.isNullOrEmpty(appBean.getType()) && appBean.getType().contains(id)){
                appBeans.add(appBean);
            }
        }
        return appBeans;
    }

    /**
     * 根据关键字模糊查询
     * @param words
     * @return
     */
    public List<AppBean> queryByWords(String words){
        List<AppBean> appBeans = new ArrayList<>();
        if(TextUtils.isEmpty(words)){
            return appBeans;
        }
        for (AppBean appBean : queryAll()) {
            String name = appBean.getName();
            if (name.indexOf(words.toString()) != -1 ||
                    PinyinUtils.getFirstSpell(name).startsWith(words.toString())
                    //不区分大小写
                    || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(words.toString())
                    || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(words.toString())) {
                appBeans.add(appBean);
            }
        }
        return appBeans;
    }

    /**
     * 重写查询全部的方法  根据设置动态隐藏显示不能打开的应用
     * @return
     */
    @Override
    public List<AppBean> queryAll() {
        if(Sputil.read(BaseApplication.getContext().getString(R.string.app_sp_hide_cant_open_app),false)){
            return mDdao.queryBuilder().where(AppBeanDao.Properties.IsCanOpen.eq(true)).list();
        }
        return super.queryAll();
    }
}
