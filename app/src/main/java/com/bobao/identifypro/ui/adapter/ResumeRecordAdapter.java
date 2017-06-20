package com.bobao.identifypro.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.utils.StringUtils;

import java.util.List;

/**
 * Created by star on 15/6/2.
 */
public class ResumeRecordAdapter extends RecyclerView.Adapter<ResumeRecordAdapter.Holder> {
    private List<String> mNames;
    private List<String> mTimes;
    private List<String> mFroms;
    private List<String> mPrices;

    public void setData(List<String> names, List<String> times, List<String> froms, List<String> prices) {
        mNames = names;
        mTimes = times;
        mFroms = froms;
        mPrices = prices;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(View.inflate(viewGroup.getContext(), R.layout.list_resume_record, null));
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        if (mPrices != null && mPrices.size() > 0 && mPrices.get(i) != null) {
            String name = mNames.get(i);
            String time = mTimes.get(i);
            String from = mFroms.get(i);
            String price = mPrices.get(i);
            holder.tvName.setText(name);
            holder.tvTime.setText(time);
            holder.tvExpense.setTextColor(TextUtils.equals(IntentConstant.RESUME_RECORD_FROM, from) ? Color.RED : Color.BLACK);
            holder.tvExpense.setText(TextUtils.equals(IntentConstant.RESUME_RECORD_FROM, from) ? StringUtils.getString("+", price, "元") : StringUtils.getString("-", price, "元"));
        }
    }

    @Override
    public int getItemCount() {
        return mPrices == null ? 0 : mPrices.size();
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
