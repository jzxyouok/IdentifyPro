package com.bobao.identifypro.ui.widget.indicator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {
    /**
     * Title text used when no title is provided by the adapter.
     */
    private static final CharSequence EMPTY_TITLE = "";
    private static final String TAG = TabPageIndicator.class.getSimpleName();

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            //Log.e("indicator", ">>>>>>TabPageIndicator onclick");
            TabView tabView = (TabView) view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    private final IcsLinearLayout mTabLayout;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
    private TabView mWallpaperTab;
    private TabView mOnlineTab;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;

    private int mTabViewPaddingLeft;
    private int mTabViewPaddingRight;

    private OnTabReselectedListener mTabReselectedListener;

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(true);

        mTabLayout = new IcsLinearLayout(context, R.attr.vpiTabPageIndicatorStyle);
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    public void updateSelectedTabView() {
        for (int i = 0; i < mTabLayout.getChildCount(); i++) {
            TabView tabView = (TabView) mTabLayout.getChildAt(i);
            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter instanceof IconPagerAdapter) {
                IconPagerAdapter iconAdapter = (IconPagerAdapter) adapter;
                // 设置文字颜色
                tabView.setTextColor(iconAdapter.getTextColor(i));
                // 设置上下的Icon
//                Bitmap bitmapBottom = BitmapUtils.getEmptyBitmap(getContext(), tabView.getWidth() - tabView.getPaddingLeft() - tabView.getPaddingRight(), 2, iconAdapter.getIconBottomColor(i));
//                Drawable drawableBottom = new BitmapDrawable(getResources(), bitmapBottom);
                Drawable drawableTop = iconAdapter.getIconTopResId(i) == 0 ? null : getContext().getResources().getDrawable(iconAdapter.getIconTopResId(i));
                tabView.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
            }
            // 文字居中
            tabView.setGravity(Gravity.CENTER);
            // 设置padding,左上右下
            tabView.setPadding(mTabViewPaddingLeft ,0, mTabViewPaddingRight, 10);
        }

    }

    private void addTab(int index, CharSequence text) {
        final TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setOnClickListener(mTabClickListener);
        tabView.setText(text);
        tabView.setPadding(mTabViewPaddingLeft, 0, mTabViewPaddingRight, 0);

        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT, 1));
    }


    public void disableOnlineDot() {
        if (mOnlineTab != null) {
            mOnlineTab.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }
//
//    public void enableOnlineDot(){
//        if(mOnlineTab != null){
//            Drawable dr = getResources().getDrawable(R.drawable.wallpaper_enable_point);
//            mOnlineTab.setCompoundDrawablesWithIntrinsicBounds(null,null,dr,null);
//        }
//    }
//
//    public void disableWallpaper(){
//        if(mWallpaperTab != null){
//            mWallpaperTab.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
//        }
//    }
//
//    public void enableWallpaper(){
//        if(mWallpaperTab != null){
//            Drawable dr = getResources().getDrawable(R.drawable.wallpaper_enable_point);
//            mWallpaperTab.setCompoundDrawablesWithIntrinsicBounds(null,null,dr,null);
//        }
//    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        updateSelectedTabView();
        if (mListener != null) {
            mListener.onPageSelected(position);
        }

        if (position == 1) {//onlien对应第二个tab
            disableOnlineDot();
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);

        notifyDataSetChanged();
    }

    private CharSequence title;

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            addTab(i, title);
        }
        updateSelectedTabView();
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    public void setTabViewPaddingRight(int tabViewPaddingRight) {
        mTabViewPaddingRight = tabViewPaddingRight;
    }

    public void setTabViewPaddingLeft(int tabViewPaddingLeft) {
        mTabViewPaddingLeft = tabViewPaddingLeft;
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.vpiTabPageIndicatorStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Re-measure if we went beyond our maximum size.
            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }

        public int getIndex() {
            return mIndex;
        }

    }
}
