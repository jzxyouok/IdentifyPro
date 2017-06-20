package com.bobao.identifypro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.ui.activity.ExpertListActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.UmengUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by star on 15/6/8.
 */
public class OrganizationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Data> mList;
    private int mIdentifyType;
    private String mIdentifyTypeName;

    public OrganizationAdapter(Context context, int identifyType, String identifyTypeName) {
        mContext = context;
        mIdentifyType = identifyType;
        mIdentifyTypeName = identifyTypeName;
        mList = new ArrayList<>();
        mList.add(new Data(R.drawable.icon_capital_museum, R.drawable.icon_capital_museum_press, R.string.capital_museum, 2));
        mList.add(new Data(R.drawable.icon_palace_museum, R.drawable.icon_palace_museum_press, R.string.palace_museum, 1));
        mList.add(new Data(R.drawable.icon_national_museum, R.drawable.icon_national_museum_press, R.string.national_museum, 3));
        mList.add(new Data(R.drawable.icon_cultural_relics_bureau, R.drawable.icon_cultural_relics_bureau_press,
                R.string.cultural_relics_bureau, 4));
        mList.add(new Data(R.drawable.icon_association_school, R.drawable.icon_association_school_press, R.string.association_school, 5));
        mList.add(new Data(R.drawable.icon_caoss, R.drawable.icon_caoss_press, R.string.chinese_academy_of_social_sciences, 6));
        mList.add(new Data(R.drawable.icon_deal, R.drawable.icon_deal_press, R.string.deal, 7));
        mList.add(new Data(R.drawable.icon_collect_world, R.drawable.icon_collect_world_press, R.string.collect, 8));
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentHolder(View.inflate(parent.getContext(), R.layout.list_item_organization, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentHolder contentHolder = (ContentHolder) holder;
        Data data = mList.get(position);
        contentHolder.imageView.setImageResource(data.imageRes);
        contentHolder.textView.setText(data.descRes);
        contentHolder.contentView.setTag(R.id.tag_position, position);
        contentHolder.contentView.setOnClickListener(this);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(R.id.tag_position);
        Intent intent = new Intent(mContext, ExpertListActivity.class);
        intent.putExtra(IntentConstant.EXPERT_LIST_ENTRANCE, IntentConstant.EXPERT_CLASSIFICATION);
        intent.putExtra(IntentConstant.ORGANIZATION_ID, mList.get(position).id);
        intent.putExtra(IntentConstant.ORGANIZATION_NAME, mList.get(position).descRes);
        intent.putExtra(IntentConstant.IDENTIFY_TYPE, mIdentifyType);
        intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, mIdentifyTypeName);
        ActivityUtils.jump(mContext, intent);
        HashMap<String, String> map = new HashMap<>();
        map.put(UmengConstants.KEY_ORGANAIZATION_ID, mList.get(position).id + "");
        UmengUtils.onEvent(mContext, EventEnum.ExpertPageOrganizationItemClick, map);
    }

    private class ContentHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private View contentView;

        public ContentHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_category);
            textView = (TextView) itemView.findViewById(R.id.tv_category);
        }
    }

    private class Data {
        private int imageRes;
        private int imagePressRes;
        private int descRes;
        private int id;

        private Data(int imageRes, int imagePressRes, int descRes, int id) {
            this.imageRes = imageRes;
            this.imagePressRes = imagePressRes;
            this.descRes = descRes;
            this.id = id;
        }

    }
}
