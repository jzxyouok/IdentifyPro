package com.bobao.identifypro.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.ui.adapter.ResumeRecordAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

public class ResumeDetailActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.srl_list)
    private SwipyRefreshLayout mRefreshLayout;
    @ViewInject(R.id.rcv_view)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.img_back_top)
    private ImageView mImgBackTop;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.tv_back)
    private TextView mTvBack;

    private ResumeRecordAdapter mResumeRecordAdapter;

    private ArrayList<String> mNames;
    private ArrayList<String> mTimes;
    private ArrayList<String> mFroms;
    private ArrayList<String> mPrices;

    @Override
    protected void getIntentData() {
        mNames = getIntent().getStringArrayListExtra(IntentConstant.RESUME_DETAIL_NAME);
        mTimes = getIntent().getStringArrayListExtra(IntentConstant.RESUME_DETAIL_TIME);
        mFroms = getIntent().getStringArrayListExtra(IntentConstant.RESUME_DETAIL_FROM);
        mPrices = getIntent().getStringArrayListExtra(IntentConstant.RESUME_DETAIL_PRICE);
    }

    @Override
    protected void initData() {
        mResumeRecordAdapter = new ResumeRecordAdapter();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_resume_detail;
    }

    @Override
    protected void initTitle() {
        ViewUtils.inject(this, mRootView);
        mTvTitle.setText(R.string.user_consume);
        setOnClickListener(mTvBack);
    }

    @Override
    protected void initContent() {
        // 下拉刷新，下拉加载更多
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        setOnClickListener(mImgBackTop);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        mRefreshLayout.setRefreshing(false);
        if (mPrices != null && mPrices.size() > 0) {
            mResumeRecordAdapter.setData(mNames, mTimes, mFroms, mPrices);
            mRecyclerView.setAdapter(mResumeRecordAdapter);
        }
    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.img_back_top:
                mRecyclerView.scrollToPosition(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        attachData();
    }
}
