package com.bobao.identifypro.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.ui.adapter.PhotoGalleryPagerAdapter;
import com.bobao.identifypro.utils.StringUtils;

import java.util.ArrayList;

import uk.co.senab.photoview.HackyViewPager;

/**
 * Created by you on 2015/7/9.
 */
public class PhotoGalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ArrayList<String> mPhotoUrls;
    private ArrayList<String> mPhotoRatios;
    private int mPhotoIndex;
    private HackyViewPager mViewPager;
    private TextView mTitleView;
    private PhotoGalleryPagerAdapter mAdapter;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mPhotoUrls = intent.getStringArrayListExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_URLS);
        mPhotoRatios = intent.getStringArrayListExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_RATIOS);
        mPhotoIndex = intent.getIntExtra(IntentConstant.ORDER_DETAIL_BANNER_IMG_INDEX, 0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_photo_gallery;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        setOnClickListener(backView);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        if (mPhotoUrls != null && mPhotoUrls.size() > 0) {
            mTitleView.setText(StringUtils.getString(mPhotoIndex + 1, "/", mPhotoUrls.size()));
        }
    }

    @Override
    protected void initContent() {
        mViewPager = (HackyViewPager) findViewById(R.id.vp_photos);
        mAdapter = new PhotoGalleryPagerAdapter(mPhotoUrls, mPhotoRatios, this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPhotoIndex);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPhotoUrls != null && mPhotoUrls.size() > 0) {
            mTitleView.setText(StringUtils.getString(position + 1, "/", mPhotoUrls.size()));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
