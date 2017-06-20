
package com.daimajia.slider.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class RatioSliderLayout extends SliderLayout {
    private float ratio = 0.0f;



    public RatioSliderLayout(Context context) {
        this(context, null);
    }

    public RatioSliderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SliderStyle);
    }

    public RatioSliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SliderLayout,
                defStyle, 0);
        ratio = attributes.getFloat(R.styleable.SliderLayout_slider_layout_ratio, 0.0f);



        attributes.recycle();

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(ratio > 0){
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
            int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
            if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {
                height = (int) (width / ratio + 0.5f);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(),
                        MeasureSpec.EXACTLY);
            } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {
                width = (int) (height * ratio + 0.5f);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(),
                        MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
