package com.bobao.identifypro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.ui.activity.ServiceNoteActivity;
import com.bobao.identifypro.ui.widget.sticker.scroll.RecyclerWithHeaderAdapter;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Arrays;

/**
 * Created by star on 16/1/10.
 */
public class StickerServiceAdapter extends RecyclerWithHeaderAdapter<StickerServiceAdapter.ServiceViewHolder> implements View.OnClickListener {
    private Context mContext;
    private static String mType;
    private int[] mBigImg = new int[]{};
    private int[] mFontImg = new int[]{};
    private int[] mIds = new int[]{};

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_service_type:
                int typeId = (int) v.getTag(R.id.service_type);
                Intent intent = new Intent(mContext, ServiceNoteActivity.class);
                intent.putExtra(IntentConstant.SERVICE_TYPE, StringUtils.getString(typeId));
                intent.putExtra(IntentConstant.SERVICE_TYPE_NAME, AppConstant.SERVICE_TYPE_NAME[typeId - 1]);
                ActivityUtils.jump(mContext, intent);
                break;
            default:
                break;
        }
    }

    public StickerServiceAdapter(Context context, String type) {
        mContext = context;
        mType = type;
        if (TextUtils.equals(AppConstant.HOME_SERVICE_TYPE[0], mType)) {
            mBigImg = Arrays.copyOfRange(AppConstant.SERVICE_TYPE_BIG_IMG, 0, 4);
            mFontImg = Arrays.copyOfRange(AppConstant.SERVICE_TYPE_FONT_IMG, 0, 4);
            mIds = Arrays.copyOfRange(AppConstant.SERVICE_TYPE_ID, 0, 4);
        } else if (TextUtils.equals(AppConstant.HOME_SERVICE_TYPE[1], mType)) {
            mBigImg = Arrays.copyOfRange(AppConstant.SERVICE_TYPE_BIG_IMG, 4, 8);
            mFontImg = Arrays.copyOfRange(AppConstant.SERVICE_TYPE_FONT_IMG, 4, 8);
            mIds = Arrays.copyOfRange(AppConstant.SERVICE_TYPE_ID, 4, 8);
        }
    }

    @Override
    public int getItemCount() {
        return mIds == null ? 1 : mIds.length + 1;
    }

    @Override
    public ServiceViewHolder oncreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ServiceViewHolder(View.inflate(mContext, R.layout.list_item_home_service, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position > 0) {
            position -= 1;
            ServiceViewHolder viewHolder = (ServiceViewHolder) holder;
            viewHolder.sdv_service_item.setImageResource(mBigImg[position]);
            viewHolder.sdv_service_item_des.setImageResource(mFontImg[position]);
            viewHolder.fl_service_type.setTag(R.id.service_type, mIds[position]);
            viewHolder.fl_service_type.setOnClickListener(this);
        }
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout fl_service_type;
        private SimpleDraweeView sdv_service_item;
        private SimpleDraweeView sdv_service_item_des;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            fl_service_type = (FrameLayout) itemView.findViewById(R.id.fl_service_type);
            sdv_service_item = (SimpleDraweeView) itemView.findViewById(R.id.sdv_service_item);
            sdv_service_item_des = (SimpleDraweeView) itemView.findViewById(R.id.sdv_service_item_des);
        }
    }
}
