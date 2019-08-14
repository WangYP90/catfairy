package com.tj24.library_base.common.recyclerview.itemTouchhelper;

/**
 * Created by energy on 2018/1/23.
 */

public interface IDragAdapter {
    //数据删除
    boolean onItemMoved(int position, int target);
}
