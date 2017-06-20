package com.bobao.identifypro.ui.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobao.identifypro.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by you on 2015/7/9.
 */
public class PhotoGalleryPagerAdapter extends PagerAdapter {
    private Activity mActivity;
    private ArrayList<String> mData;
    private ArrayList<String> mRatios;

    private PhotoView mPhotoView;
    private PhotoViewAttacher mPhotoViewAttacher;

    public PhotoGalleryPagerAdapter(ArrayList<String> data, ArrayList<String> ratios, Activity activity) {
        mData = data;
        mRatios = ratios;
        mActivity = activity;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.list_item_photo_gallery, null);

        mPhotoView = (PhotoView) itemView.findViewById(R.id.sdv_photo);
        mPhotoViewAttacher = new PhotoViewAttacher(mPhotoView);
        //点击事件
        mPhotoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                mActivity.finish();
            }

            @Override
            public void onOutsidePhotoTap() {
                mActivity.finish();
            }
        });
        if (!TextUtils.isEmpty(mData.get(position))) {
            Picasso.with(mActivity).load(mData.get(position)).into(mPhotoView);
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
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
