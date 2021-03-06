package com.tj24.appmanager.adapter;

import android.view.ViewGroup;

import com.tj24.appmanager.common.AppConst;
import com.tj24.appmanager.daohelper.AppBeanDaoHelper;
import com.tj24.appmanager.fragment.AppsFragment;
import com.tj24.base.bean.appmanager.AppBean;
import com.tj24.base.bean.appmanager.AppClassfication;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * @Description:viewpager的Apdapter
 * @Createdtime:2019/3/17 23:03
 * @Author:TangJiangs
 * @Version: V.1.0.0
 */
public class AppsVpAdater extends FragmentStatePagerAdapter {
    //对应的type
    private List<AppClassfication> appClassfications = new ArrayList<>();
    //当前fragment
    private AppsFragment mCurrentFragment;

    public AppsVpAdater(FragmentManager fm,List<AppClassfication> appClassfications) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
        if(classfication.getId().equals(AppConst.CLASSFICATION_SYSTEM_ID)){
            return AppBeanDaoHelper.getInstance().querySystems();
        }else if(classfication.getId().equals(AppConst.CLASSFICATION_CUSTOM_ID)){
            return AppBeanDaoHelper.getInstance().queryCutomes();
        }
        return AppBeanDaoHelper.getInstance().queryAppByClassficationId(classfication.getId());
    }

    /**
     * 返回 POSITION_NONE 可以解决 AppsVpAdater刷新 item不刷新的问题
      * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     *配合这个方法可以获取到显示的是哪个fragment
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (AppsFragment) object;
        super.setPrimaryItem(container, position, object);
    }


    public AppsFragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
