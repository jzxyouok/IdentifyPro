package com.bobao.identifypro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.domain.ExpertDetailResponse;
import com.bobao.identifypro.ui.activity.ServiceNoteActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Fang on 2016/3/18.
 */
public class ExpertDetailServiceAdapter extends RecyclerView.Adapter<ExpertDetailServiceAdapter.ContentHolder> implements View.OnClickListener {
    private Context mContext;
    private List<ExpertDetailResponse.DataEntity.ServerEntity.OrdinaryEntity> mOrdinaryEntityList;
    private List<ExpertDetailResponse.DataEntity.ServerEntity.SpecialEntity> mSpecialEntityList;
    private int mDataStatus;
    private String mIdentifyType;
    private String mIdentifyName;
    private String mExpertId;
    private String mExpertName;

    public ExpertDetailServiceAdapter(Context mContext, int dataStatus) {
        this.mContext = mContext;
        mDataStatus = dataStatus;
    }

    public void setIntentData(String identifyType, String identifyName, String expertId, String expertName) {
        mIdentifyType = identifyType;
        mIdentifyName = identifyName;
        mExpertId = expertId;
        mExpertName = expertName;
    }

    public void resetData(ExpertDetailResponse.DataEntity.ServerEntity serverEntity) {
        if (IntentConstant.EXPERTS_DETAIL_ORDINARY_SERVICE == mDataStatus) {
            mOrdinaryEntityList = serverEntity.getOrdinary();
        } else if (IntentConstant.EXPERTS_DETAIL_SPECIAL_SERVICE == mDataStatus) {
            mSpecialEntityList = serverEntity.getSpecial();
        }
        notifyDataSetChanged();
    }

    @Override
    public ExpertDetailServiceAdapter.ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentHolder(View.inflate(parent.getContext(), R.layout.list_item_expert_detail_service, null));
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        int id = 0;
        String img = "";
        String name = "";
        String appraisalCost = "";
        if (IntentConstant.EXPERTS_DETAIL_ORDINARY_SERVICE == mDataStatus){//若是服务项目
            if (mOrdinaryEntityList != null && mOrdinaryEntityList.size() > 0 && "0".equals(mOrdinaryEntityList.get(position).getState()) ){
              holder.view.setVisibility(View.GONE);
            }
        }
        if (IntentConstant.EXPERTS_DETAIL_ORDINARY_SERVICE == mDataStatus && mOrdinaryEntityList != null && mOrdinaryEntityList.size() > 0) {
            id = mOrdinaryEntityList.get(position).getId();
            img = mOrdinaryEntityList.get(position).getImage();
            name = mOrdinaryEntityList.get(position).getName();
            appraisalCost =
                    StringUtils.getString(mOrdinaryEntityList.get(position).getPrice(), mContext.getString(R.string.unit_of_money), mOrdinaryEntityList.get(position).getUnit());
        } else if (IntentConstant.EXPERTS_DETAIL_SPECIAL_SERVICE == mDataStatus && mSpecialEntityList != null && mSpecialEntityList.size() > 0) {
            id = mSpecialEntityList.get(position).getId();
            img = mSpecialEntityList.get(position).getImage();
            name = mSpecialEntityList.get(position).getName();
            appraisalCost =
                    StringUtils.getString(mSpecialEntityList.get(position).getPrice(), mContext.getString(R.string.unit_of_money), mSpecialEntityList.get(position).getUnit());
        }
        if (!TextUtils.isEmpty(img)) {
            holder.sdv_service.setImageURI(Uri.parse(img));
        }
        holder.tv_service.setText(name);
        holder.mItemView.setTag(R.id.service_type, id);
        holder.mItemView.setTag(R.id.service_name, name);
        holder.mItemView.setTag(R.id.appraisal_cost, appraisalCost);
        holder.mItemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (IntentConstant.EXPERTS_DETAIL_ORDINARY_SERVICE == mDataStatus && mOrdinaryEntityList != null) {
            count = mOrdinaryEntityList == null ? 0 : mOrdinaryEntityList.size();
        } else if (IntentConstant.EXPERTS_DETAIL_SPECIAL_SERVICE == mDataStatus && mSpecialEntityList != null) {
            count = mSpecialEntityList == null ? 0 : mSpecialEntityList.size();
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        int typeId = (int) v.getTag(R.id.service_type);
        String typeName = (String) v.getTag(R.id.service_name);
        String appraisalCost = (String) v.getTag(R.id.appraisal_cost);
        Intent intent = new Intent(mContext, ServiceNoteActivity.class);
        intent.putExtra(IntentConstant.SERVICE_TYPE, StringUtils.getString(typeId));
        intent.putExtra(IntentConstant.SERVICE_TYPE_NAME, typeName);
        intent.putExtra(IntentConstant.IDENTIFY_TYPE, mIdentifyType);
        intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, mIdentifyName);
        intent.putExtra(IntentConstant.EXPERT_ID, mExpertId);
        intent.putExtra(IntentConstant.EXPERT_NAME, mExpertName);
        intent.putExtra(IntentConstant.APPRAISAL_COST, appraisalCost);
        ActivityUtils.jump(mContext, intent);
    }

    public class ContentHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        private SimpleDraweeView sdv_service;
        private TextView tv_service;
        private View view ;

        public ContentHolder(View itemView) {
            super(itemView);
            view = itemView;
            mItemView = itemView;
            sdv_service = (SimpleDraweeView) itemView.findViewById(R.id.sdv_service);
            tv_service = (TextView) itemView.findViewById(R.id.tv_service);
        }
    }
}
