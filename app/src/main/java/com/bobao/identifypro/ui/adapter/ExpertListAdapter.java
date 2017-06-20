package com.bobao.identifypro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.application.IdentifyProApplication;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.domain.ExpertListResponse;
import com.bobao.identifypro.ui.activity.ExpertDetailActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by star on 15/6/8.
 */
public class ExpertListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<ExpertListResponse.DataEntity> mList;

    private OrganizationAdapter mAdapter;

    private int mIdentifyType;
    private String mIdentifyTypeName;
    private boolean mIsQuestNoData;

    public ExpertListAdapter(Context context, int identifyType, String identifyTypeName) {
        mContext = context;
        mList = new ArrayList<>();
        mIdentifyType = identifyType;
        mIdentifyTypeName = identifyTypeName;
    }

    public ExpertListAdapter(Context context, OrganizationAdapter adapter, int identifyType, String identifyTypeName) {
        mContext = context;
        mList = new ArrayList<>();
        mAdapter = adapter;
        mIdentifyType = identifyType;
        mIdentifyTypeName = identifyTypeName;
    }

    public void addData(List<ExpertListResponse.DataEntity> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void resetData(List<ExpertListResponse.DataEntity> list) {
        if (list.size() == 0){
            mIsQuestNoData = true;
            mList = (ArrayList<ExpertListResponse.DataEntity>) IdentifyProApplication.getIntentData(IntentConstant.SAVE_EXPERT_DATA);
        }else{
            mList.clear();
            IdentifyProApplication.setIntentData(IntentConstant.SAVE_EXPERT_DATA,list);
            mList.addAll(list);
            mIsQuestNoData = false;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == 0 && mAdapter != null) {
            type = Types.header;
        }else{
            type = Types.normal;
        }
        if (position == 0 && mIsQuestNoData && mAdapter == null){
            type = Types.header;
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Types.header:
                view = View.inflate(parent.getContext(), R.layout.header_expert, null);
                return new HeaderHolder(view);
            case Types.normal:
                view = View.inflate(parent.getContext(), R.layout.list_item_expert, null);
                return new ContentHolder(view);
            default:
                return null;
        }
    }
    private boolean mIsTitleShow = false;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            if (mIsQuestNoData){
                mIsTitleShow = true;
                headerHolder.tvPrompt.setVisibility(View.VISIBLE);
                headerHolder.expertTuiTitle.setVisibility(View.VISIBLE);
                headerHolder.expertListTitle.setVisibility(View.GONE);
                headerHolder.organizationView.setVisibility(View.GONE);
            }else{
                mIsTitleShow = true;
                headerHolder.expertListTitle.setVisibility(View.VISIBLE);
                headerHolder.organizationView.setVisibility(View.VISIBLE);
                headerHolder.tvPrompt.setVisibility(View.GONE);
                headerHolder.expertTuiTitle.setVisibility(View.GONE);
                headerHolder.organizationView.setAdapter(mAdapter);
            }
        }
        if (holder instanceof ContentHolder) {
            int index;
            if (position == 0 && mIsTitleShow){
                return;
            }
            if (!mIsTitleShow){
                index = position;
            }else{
                index = position - 1;
            }
            ContentHolder contentHolder = (ContentHolder) holder;
            contentHolder.portraitView.setImageURI(Uri.parse(mList.get(index).getHead_img()));
            if (TextUtils.equals("1", mList.get(index).getTui())) {
                contentHolder.recommend.setVisibility(View.VISIBLE);
            } else {
                contentHolder.recommend.setVisibility(View.GONE);
            }
            contentHolder.nameView.setText(mList.get(index).getName());
            contentHolder.descView.setText(mList.get(index).getHonors());
            contentHolder.goodAtView.setText(mList.get(index).getKind());
            contentHolder.containerView.setTag(index);
            contentHolder.containerView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        int count;
        if (mAdapter == null) {
            count = mList.size();
        }else{
            count = mList.size() + 1;
        }
        if (mAdapter == null && mIsQuestNoData){
            count = mList.size() + 1;
        }
        return count;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ExpertDetailActivity.class);
        int pos = (int) v.getTag();
        ExpertListResponse.DataEntity entity = mList.get(pos);
        intent.putExtra(IntentConstant.EXPERT_ID, entity.getId());
        intent.putExtra(IntentConstant.IDENTIFY_TYPE, String.valueOf(mIdentifyType));
        intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, mIdentifyTypeName);
        ActivityUtils.jump(v.getContext(), intent);
        HashMap<String, String> map = new HashMap<>();
        map.put(UmengConstants.KEY_EXPERT_PAGE_ITEM_ID, entity.getId());
        UmengUtils.onEvent(v.getContext(), EventEnum.ExpertPageListItemClick, map);
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        private RecyclerView organizationView;
        private TextView tvPrompt;
        private View expertListTitle;
        private View expertTuiTitle;
        public HeaderHolder(View itemView) {
            super(itemView);
            tvPrompt = (TextView) itemView.findViewById(R.id.tv_prompt);
            expertListTitle = itemView.findViewById(R.id.rl_expert_list);
            expertTuiTitle = itemView.findViewById(R.id.rl_tui_expert);
            organizationView = (RecyclerView) itemView.findViewById(R.id.rcv_organization);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, IntentConstant.RECYCLERVIEW_SIZE_GRIDLAYOUT_FOUR);
            organizationView.setLayoutManager(gridLayoutManager);
        }
    }

    private class ContentHolder extends RecyclerView.ViewHolder {
        private View containerView;
        private SimpleDraweeView portraitView;
        private View recommend;
        private TextView nameView;
        private TextView descView;
        private TextView goodAtView;
        private View attentionView;
        private View privateView;
        private View shareView;

        public ContentHolder(View itemView) {
            super(itemView);
            containerView = itemView;
            portraitView = (SimpleDraweeView) itemView.findViewById(R.id.sdv_portrait);
            recommend = itemView.findViewById(R.id.iv_recommend);
            nameView = (TextView) itemView.findViewById(R.id.tv_name);
            descView = (TextView) itemView.findViewById(R.id.tv_desc);
            goodAtView = (TextView) itemView.findViewById(R.id.tv_good_at);
            attentionView = itemView.findViewById(R.id.tv_attention);
            privateView = itemView.findViewById(R.id.tv_private);
            shareView = itemView.findViewById(R.id.tv_commit);
        }
    }

    private static class Types {
        private static final int header = 1;
        private static final int normal = 2;
    }
}
