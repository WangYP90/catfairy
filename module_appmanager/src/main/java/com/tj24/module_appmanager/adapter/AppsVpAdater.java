package com.tj24.module_appmanager.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.tj24.module_appmanager.bean.AppBean;
import com.tj24.module_appmanager.bean.AppClassfication;
import com.tj24.module_appmanager.fragment.AppsFragment;
import com.tj24.module_appmanager.greendao.daohelper.AppBeanDaoHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:viewpager的Apdapter
 * @Createdtime:2019/3/17 23:03
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class AppsVpAdater extends FragmentStatePagerAdapter {
    //传入的appbean
    private List<AppBean> appBeans = new ArrayList<>();
    //对应的type
    private List<AppClassfication> appClassfications = new ArrayList<>();

    public AppsVpAdater(FragmentManager fm,List<AppBean> appBeans,List<AppClassfication> appClassfications) {
        super(fm);
        this.appBeans = appBeans;
        this.appClassfications = appClassfications;
    }

    @Override
    public Fragment getItem(int position) {
        AppClassfication classfication = appClassfications.get(position);
        List<AppBean> appBeans = new ArrayList<>();
        appBeans.addAll(getItemAppBeans(classfication));
        return AppsFragment.newInstance(appBeans, classfication);
    }


    @Override
    public int getCount() {
        return appClassfications.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        AppClassfication classfication = appClassfications.get(position);
        String typeName = appClassfications.get(position).getName();
        return typeName+"("+getItemAppBeans(classfication).size()+")";
    }

    /**
     * 获取选中tab的数据 并排序
     * @param classfication
     * @return
     */
    private List<AppBean> getItemAppBeans(AppClassfication classfication) {
        return AppBeanDaoHelper.getInstance().queryAppByClassficationId(classfication.getId());
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
