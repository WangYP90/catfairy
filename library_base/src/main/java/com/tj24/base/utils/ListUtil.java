package com.tj24.base.utils;

import androidx.collection.ArraySet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description:集合相关操作类
 * @Createdtime:2019/3/10 21:18
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class ListUtil {

    /**
     * s是否为空或者为空
     * @param list
     * @return
     */
    public static boolean isNullOrEmpty(List list){
        return list == null || list.isEmpty();
    }

    /**
     * 将 数组转换为ArrayList
     * @param charSequences
     * @return
     */
    public static List toList(CharSequence[]charSequences){
        List list = new ArrayList();
        for(CharSequence charSequence : charSequences){
            list.add(charSequence);
        }
        return list;
    }

    /**
     * 将 数组转换为set
     * @param charSequences
     * @return
     */
    public static Set toSet(CharSequence[]charSequences){
        Set set = new ArraySet();
        for(CharSequence charSequence : charSequences){
            set.add(charSequence);
        }
        return set;
    }
}
