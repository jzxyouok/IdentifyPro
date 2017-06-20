package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.IntegrateDetailResponse;
import com.bobao.identifypro.ui.adapter.IntegrateDetailAdapter;
import com.bobao.identifypro.utils.NetUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.List;

public class IntegrateDetailActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener {
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

    private IntegrateDetailAdapter mIntegrateRecordAdapter;

    private int mCurrentPage;

    @Override
    protected void getIntentData() {
    }

    @Override
    protected void initData() {
        mCurrentPage = 1;
        mIntegrateRecordAdapter = new IntegrateDetailAdapter();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_resume_detail;
    }

    @Override
    protected void initTitle() {
        ViewUtils.inject(this, mRootView);
        mTvTitle.setText(R.string.integrate_detail);
        setOnClickListener(mTvBack);
    }

    @Override
    protected void initContent() {
        // 下拉刷新，下拉加载更多
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mRefreshLayout
                .setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mIntegrateRecordAdapter);
        setOnClickListener(mImgBackTop);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        NetUtils.getInstance(false).get(mContext, NetConstant.getIntegrateDetailParams(mContext, mCurrentPage), new IntegrateDetailListener(mContext));
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
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            //下拉刷新
            mCurrentPage = 1;
            attachData();
        } else {
            mCurrentPage++;
            attachData();
        }
    }

    protected void attachData(List<IntegrateDetailResponse.DataEntity> list) {
        if (mCurrentPage == 1) {
            mIntegrateRecordAdapter.resetData(list);
        } else {
            mIntegrateRecordAdapter.addData(list);
        }
    }

    private class IntegrateDetailListener extends NetUtils.Callback<IntegrateDetailResponse> {

        public IntegrateDetailListener(Context context) {
            super(context, IntegrateDetailResponse.class);
        }

        @Override
        public void onNetSuccess(IntegrateDetailResponse response) {
            mRefreshLayout.setRefreshing(false);
            if (response.getError() || response.getData() == null || response.getData().size() == 0) {
                return;
            }
            attachData(response.getData());
        }

        @Override
        public void onConvertFailed(String json) {
            mRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mRefreshLayout.setRefreshing(false);
        }

    }
}
