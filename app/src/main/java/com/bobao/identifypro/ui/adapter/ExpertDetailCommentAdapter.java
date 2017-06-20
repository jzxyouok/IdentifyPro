package com.bobao.identifypro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.domain.ExpertDetailResponse;
import com.bobao.identifypro.ui.activity.ServiceAppointmentActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ExpertDetailCommentAdapter extends BaseAdapter implements View.OnClickListener {

    private List<ExpertDetailResponse.DataEntity.CommentArrEntity> mList;
    private Context mContext;
    private String test;

    public ExpertDetailCommentAdapter(Context mContext) {
        mList = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setData(List<ExpertDetailResponse.DataEntity.CommentArrEntity> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ExpertDetailResponse.DataEntity.CommentArrEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(parent.getContext(), R.layout.list_item_expert_comment, null);
            holder.portrait = (SimpleDraweeView) convertView.findViewById(R.id.sdv_portrait);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.mRbLabel = (RatingBar) convertView.findViewById(R.id.rb_label);
            holder.mRbLabelLL = (RelativeLayout) convertView.findViewById(R.id.rl_rb_label);
            holder.mRbLabelLL.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ExpertDetailResponse.DataEntity.CommentArrEntity data = getItem(position);
        holder.portrait.setImageURI(Uri.parse(data.getHead_img()));
        holder.name.setText(data.getNikename());
        holder.time.setText(data.getAddtime());
        holder.mRbLabel.setRating(TextUtils.isEmpty(data.getStars()) ? 0 : Float.valueOf(data.getStars()));
        holder.content.setText(data.getContext());
        convertView.setTag(R.id.tag_order_id, data.getGoods_id());
        test = data.getGoods_id();
//        holder.mNoEvaluate.setVisibility(!data.getContext().equals("") ? View.GONE : View.VISIBLE);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, ServiceAppointmentActivity.class);
        intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY);
        intent.putExtra(IntentConstant.APPOINTMENT_IDENTIFY_ID, (String) v.getTag(R.id.tag_order_id));
        intent.putExtra(IntentConstant.APPOINTMENT_IDENTIFY_TYPE, IntentConstant.APPOINTMENT_IDENTIFY_DETAIL);
        ActivityUtils.jump(mContext, intent);
    }

    private class Holder {
        private SimpleDraweeView portrait;
        private TextView name;
        private TextView time;
        private TextView content;
        private RatingBar mRbLabel;
        private RelativeLayout mRbLabelLL;
    }
}
