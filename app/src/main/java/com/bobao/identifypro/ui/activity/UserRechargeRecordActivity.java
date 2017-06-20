package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.DealRecordReponse;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.lidroid.xutils.exception.HttpException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by you on 2015/6/15.
 */
public class UserRechargeRecordActivity extends BaseActivity {
    private RecyclerView mRecordRecyclerView;
    private View mLoadFailedUI;
    private View mLoadingUI;
    private TextView mLoadFailedTipTv;
    private Button mLoadFailedBtn;
    private String mUserId;
    private List<DealRecordReponse.DataEntity> mData = new ArrayList<>();

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mUserId = intent.getStringExtra(IntentConstant.USER_ID);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_recharge_record;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText("我的交易记录");
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mRecordRecyclerView = (RecyclerView) findViewById(R.id.rcv_recharg_record);
        LinearLayoutManager lv = new LinearLayoutManager(this);
        mRecordRecyclerView.setLayoutManager(lv);

        mLoadingUI = findViewById(R.id.vg_loading);
        mLoadingUI.setVisibility(View.GONE);

        mLoadFailedUI = findViewById(R.id.vg_no_data);
        mLoadFailedUI.setVisibility(View.GONE);
        mLoadFailedTipTv = (TextView) mLoadFailedUI.findViewById(R.id.tv_no_data_tip);
        mLoadFailedBtn = (Button) mLoadFailedUI.findViewById(R.id.btn_no_data_action);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            default:
                super.onClick(view);
        }
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    private void startUserRechargeRecordRequest() {
        mLoadingUI.setVisibility(View.VISIBLE);
        mLoadFailedUI.setVisibility(View.GONE);
        NetUtils.getInstance(false).get(mContext, NetConstant.getUserRechargeRecordParams(mContext), new UserRechargeRecordListener(mContext));
    }

    private void handleLoadFailedUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mRecordRecyclerView.setVisibility(View.GONE);
        mLoadFailedTipTv.setText("获取充值记录失败");
        mLoadFailedBtn.setText("重试");
        mLoadFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserRechargeRecordRequest();
            }
        });
    }

    private void handleNoDataUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mRecordRecyclerView.setVisibility(View.GONE);
        mLoadFailedTipTv.setText("您还没有充值记录");
        mLoadFailedBtn.setText("我要充值");
        mLoadFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //我要充值
                Intent intent = new Intent(UserRechargeRecordActivity.this, UserRechargeSelectActivity.class);
                intent.putExtra(IntentConstant.USER_ID, mUserId);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void refreshData() {
        startUserRechargeRecordRequest();
    }

    class UserRechargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(UserRechargeRecordActivity.this).inflate(R.layout.list_score_record, null);
            return new DealRecordViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            DealRecordReponse.DataEntity entity = mData.get(position);
            DealRecordViewHolder vh = (DealRecordViewHolder) holder;
            vh.tv_item_scrore_record_time.setText(entity.getTime());

            vh.tv_item_scrore_record_introduce.setText(entity.getType_name());
            String from = entity.getFrom();
            if (TextUtils.equals(from, "enter")) {
                vh.tv_item_scrore_record_num.setTextColor(Color.RED);
                vh.tv_item_scrore_record_num.setText(StringUtils.getString("+", entity.getPrice(), "元"));
            } else {
                vh.tv_item_scrore_record_num.setTextColor(Color.BLACK);
                vh.tv_item_scrore_record_num.setText(entity.getNote());
            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }
    }

    class DealRecordViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_scrore_record_time;
        private TextView tv_item_scrore_record_num;
        private TextView tv_item_scrore_record_introduce;

        public DealRecordViewHolder(View itemView) {
            super(itemView);
            tv_item_scrore_record_time = (TextView) itemView.findViewById(R.id.tv_item_scrore_record_time);
            tv_item_scrore_record_num = (TextView) itemView.findViewById(R.id.tv_item_scrore_record_num);
            tv_item_scrore_record_introduce = (TextView) itemView.findViewById(R.id.tv_item_scrore_record_introduce);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }

    private class UserRechargeRecordListener extends NetUtils.Callback<DealRecordReponse> {
        public UserRechargeRecordListener(Context context) {
            super(context, DealRecordReponse.class);
        }

        @Override
        public void onConvertFailed(String json) {
            mLoadingUI.setVisibility(View.GONE);
            handleLoadFailedUI();
        }

        @Override
        public void onNetSuccess(DealRecordReponse response) {
            mLoadingUI.setVisibility(View.GONE);
            if (response == null || response.getError()) {
                Toast.makeText(UserRechargeRecordActivity.this, "获取充值记录失败", Toast.LENGTH_SHORT).show();
                handleLoadFailedUI();
                return;
            }
            mData = response.getData();
            if (mData == null || mData.size() == 0) {
                //没有充值记录UI
                handleNoDataUI();
            } else {
                mLoadFailedUI.setVisibility(View.GONE);
                mRecordRecyclerView.setVisibility(View.VISIBLE);
                UserRechargeAdapter adapter = new UserRechargeAdapter();
                mRecordRecyclerView.setAdapter(adapter);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mLoadingUI.setVisibility(View.GONE);
            handleLoadFailedUI();
        }
    }
}
