package com.tj24.appmanager.common;

import android.transition.Transition;
import androidx.annotation.NonNull;

/**
 * 空实现 避免 使用过程中每次都要实现所有的方法
 */
public class SimpleTransitionListener implements Transition.TransitionListener {
    @Override
    public void onTransitionStart(@NonNull Transition transition) {

    }

    @Override
    public void onTransitionEnd(@NonNull Transition transition) {

    }

    @Override
    public void onTransitionCancel(@NonNull Transition transition) {

    }

    @Override
    public void onTransitionPause(@NonNull Transition transition) {

    }

    @Override
    public void onTransitionResume(@NonNull Transition transition) {

    }
}
