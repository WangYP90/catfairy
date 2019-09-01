package com.tj24.appmanager.util;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {
    @SuppressWarnings("ResourceType")
    /**
     * 测量view
     */
    public static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }
}
