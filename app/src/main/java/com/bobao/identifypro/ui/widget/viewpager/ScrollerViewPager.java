package com.bobao.identifypro.ui.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Fang on 2016/3/14.
 */
public class ScrollerViewPager extends github.chenupt.springindicator.viewpager.ScrollerViewPager {
    public ScrollerViewPager(Context context) {
        super(context);
    }

    public ScrollerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            Log.e("IndexOutOf","onTouchEvent1");
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //不理会
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            //不理会
            return false;
        } catch (IndexOutOfBoundsException e) {
            //不理会
            Log.e("IndexOutOf","onInterceptTouchEvent");
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            Log.e("IndexOutOf","onTouchEvent1");
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //不理会
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            //不理会
            return false;
        } catch (IndexOutOfBoundsException e) {
            //不理会
            Log.e("IndexOutOf","onTouchEvent");
            return false;
        }
    }
}
