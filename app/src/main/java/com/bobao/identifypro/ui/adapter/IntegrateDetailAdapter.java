package com.bobao.identifypro.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.domain.IntegrateDetailResponse;
import com.bobao.identifypro.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IntegrateDetailAdapter extends RecyclerView.Adapter<IntegrateDetailAdapter.Holder> {
    private List<IntegrateDetailResponse.DataEntity> mDataEntities;

    public IntegrateDetailAdapter() {
        mDataEntities = new ArrayList<>();
    }

    public void addData(List<IntegrateDetailResponse.DataEntity> data) {
        mDataEntities.addAll(data);
        notifyDataSetChanged();
    }

    public void resetData(List<IntegrateDetailResponse.DataEntity> data) {
        mDataEntities.clear();
        mDataEntities.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(View.inflate(viewGroup.getContext(), R.layout.list_resume_record, null));
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        if (mDataEntities != null && mDataEntities.size() > 0 && mDataEntities.get(i) != null) {
            IntegrateDetailResponse.DataEntity dataEntity = mDataEntities.get(i);
            holder.tvName.setText(dataEntity.getNote());
            holder.tvTime.setText(dataEntity.getAddtime());
            holder.tvExpense.setTextColor(IntentConstant.INTEGRATE_APPLY == dataEntity.getState() ? Color.BLACK : Color.RED);
            holder.tvExpense.setText(IntentConstant.INTEGRATE_APPLY == dataEntity.getState() ? StringUtils.getString("-", dataEntity.getNum(), "元") :
                    StringUtils.getString("+", dataEntity.getState(), "元"));
        }
    }

    @Override
    public int getItemCount() {
        return mDataEntities == null ? 0 : mDataEntities.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvTime;
        private TextView tvExpense;

        public Holder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_resume_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvExpense = (TextView) itemView.findViewById(R.id.tv_expense);
        }
    }
}
