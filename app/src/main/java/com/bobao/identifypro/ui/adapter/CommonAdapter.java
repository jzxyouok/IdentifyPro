package com.bobao.identifypro.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by kakaxicm on 2015/11/4.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    protected Context mContext;
    protected int mLayoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId) {
        mData = data;
        mContext = context;
        mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = ViewHolder.get(mContext, convertView, parent, mLayoutId);
        convert(vh, mData.get(position));
        return vh.getConvertView();
    }

    abstract void convert(ViewHolder vh, T item);

}
