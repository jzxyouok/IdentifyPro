package com.bobao.identifypro.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobao.identifypro.R;

public class NavigationPagerAdapter extends PagerAdapter {

    private Context mContext;

    public NavigationPagerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private int[] BG_IMGS = {
            R.drawable.navigation_page1,
            R.drawable.navigation_page2};

    private View.OnClickListener mListener;

    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.list_item_navigation_page, null);
        View startBtn = itemView.findViewById(R.id.btn_navigation_start);
        View mLL = itemView.findViewById(R.id.rl_splash);

        mLL.setBackgroundResource(BG_IMGS[position]);
        if (position == getCount() - 1) {
            startBtn.setVisibility(View.VISIBLE);
            if (mListener != null) {
                startBtn.setOnClickListener(mListener);
                mLL.setOnClickListener(mListener);
            }
        } else {
            startBtn.setVisibility(View.GONE);
        }
        container.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return BG_IMGS == null ? 0 : BG_IMGS.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
