package com.bobao.identifypro.ui.widget.fix;

import android.widget.GridView;

/**
 * Created by star on 15/6/8.
 */
public class FixedGridView extends GridView {
    public FixedGridView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}