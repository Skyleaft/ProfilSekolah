package com.example.mzstudio.profilsekolah;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

/**
 * Created by Milzan Malik on 16/02/2018.
 */

public class FloatingActionMenuBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public FloatingActionMenuBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = Math.max(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        super.onDependentViewRemoved(parent, child, dependency);
    }
}
