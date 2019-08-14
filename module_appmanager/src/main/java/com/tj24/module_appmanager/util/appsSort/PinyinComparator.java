package com.tj24.module_appmanager.util.appsSort;

import com.tj24.module_appmanager.bean.AppBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<AppBean> {

	public int compare(AppBean o1, AppBean o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return -1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return 1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
