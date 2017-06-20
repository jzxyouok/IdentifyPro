package com.bobao.identifypro.ui.widget;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.bobao.identifypro.R;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.facebook.drawee.view.SimpleDraweeView;

public class BannerSliderView extends BaseSliderView {
    private String mUrl;

    public BannerSliderView(Context context) {
        super(context);
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.list_item_home_banner_picture, null);
        SimpleDraweeView view = (SimpleDraweeView) v.findViewById(R.id.sdv_picture);
        loadImage(view);
        // 加载图片
        if (!TextUtils.isEmpty(mUrl)) {
            view.setImageURI(Uri.parse(mUrl));
        }
        bindClickEvent(v);
        return v;
    }
}
