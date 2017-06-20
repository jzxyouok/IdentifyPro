package com.bobao.identifypro.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.bobao.identifypro.R;
import com.bobao.identifypro.ui.adapter.StickerServiceAdapter;
import com.bobao.identifypro.ui.widget.recycler.DividerItemDecoration;
import com.bobao.identifypro.ui.widget.sticker.StickHeaderRecyclerViewFragment;
import com.bobao.identifypro.utils.UmengUtils;

/**
 * Created by star on 15/9/26.
 */
public class StickerServiceFragment extends StickHeaderRecyclerViewFragment {
    private static StickerServiceFragment mInstance;

    private static String mType;
    private static String mTypeName;
    private StickerServiceAdapter mServiceAdapter;

    // 不完全单例
    public static StickerServiceFragment getInstance(String type, String typeName) {
        if (mInstance == null) {
            mInstance = new StickerServiceFragment();
        }
        mType = type;
        mTypeName = typeName;
        return mInstance;
    }

    @Override
    public void bindData() {
        mServiceAdapter = new StickerServiceAdapter(getActivity(),mType);
        final LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), LinearLayoutManager.VERTICAL);
        getScrollView().setLayoutManager(linearLayoutManager);
        getScrollView().addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.bg_divider));
        // 添加adapter
        getScrollView().setAdapter(mServiceAdapter);
    }

    public void refreshData(String type) {
        mType = type;
        bindData();
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onPageStart("StickerServiceFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPageStart("StickerServiceFragment");
    }

    @Override
    public String getTitle() {
        return mTypeName;
    }
}
