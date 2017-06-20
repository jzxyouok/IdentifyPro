package com.bobao.identifypro.ui.widget.scrollview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by star on 15/8/6.
 */
public class EnableTouchScrollView extends ScrollView {
    public EnableTouchScrollView(Context context) {
        super(context);
    }

    public EnableTouchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnableTouchScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        super.onTouchEvent(ev);
        return false;
    }
}
