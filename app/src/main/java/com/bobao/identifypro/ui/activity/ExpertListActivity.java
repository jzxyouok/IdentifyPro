package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.ExpertListResponse;
import com.bobao.identifypro.ui.adapter.ExpertListAdapter;
import com.bobao.identifypro.ui.adapter.OrganizationAdapter;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

public class ExpertListActivity extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener {
    private RecyclerView mExpertView;
    private SwipyRefreshLayout mRefreshLayout;

    private ExpertListAdapter mExpertAdapter;
    private List<ExpertListResponse.DataEntity> mList;

    private int mCurrentPage;

    private int mExpertListEntrance;
    // 鉴定类型
    private int mIdentifyType;
    private String mIdentifyTypeName;
    //机构名称
    private int mOrganizationId;
    private int mOrganizationName;

    @Override
    protected void getIntentData() {
        mExpertListEntrance = getIntent().getIntExtra(IntentConstant.EXPERT_LIST_ENTRANCE, 0);
        mIdentifyType = getIntent().getIntExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeChina);
        mIdentifyTypeName = getIntent().getStringExtra(IntentConstant.IDENTIFY_TYPE_NAME);
        if (IntentConstant.EXPERT_CLASSIFICATION == mExpertListEntrance) {
            mOrganizationId = getIntent().getIntExtra(IntentConstant.ORGANIZATION_ID, -1);
            mOrganizationName = getIntent().getIntExtra(IntentConstant.ORGANIZATION_NAME, R.string.app_name);
        }
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        mCurrentPage = 1;
        if (IntentConstant.EXPERT_LIST == mExpertListEntrance) {
            mExpertAdapter = new ExpertListAdapter(mContext, new OrganizationAdapter(mContext, mIdentifyType, mIdentifyTypeName), mIdentifyType, mIdentifyTypeName);
        } else if (IntentConstant.EXPERT_CLASSIFICATION == mExpertListEntrance) {
            mExpertAdapter = new ExpertListAdapter(mContext, mIdentifyType, mIdentifyTypeName);
        }
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_expert_list;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        if (IntentConstant.EXPERT_LIST == mExpertListEntrance) {
            titleView.setText(StringUtils.getString(mIdentifyTypeName, getString(R.string.expert)));
        } else if (IntentConstant.EXPERT_CLASSIFICATION == mExpertListEntrance) {
            titleView.setText(mOrganizationName);
        }
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        // 下拉刷新
        mRefreshLayout = (SwipyRefreshLayout) mRootView.findViewById(R.id.srl_expert);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
        // 专家列表
        mExpertView = (RecyclerView) mRootView.findViewById(R.id.rv_expert);
        // 设置纵向滚动
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mExpertView.setLayoutManager(manager);
        // 添加上拉加载更多
        mExpertView.addOnScrollListener(new ExpertScrollListener(manager));
        // 设置adapter
        mExpertView.setAdapter(mExpertAdapter);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        // 请求专家列表数据
        RequestParams requestParams = null;
        if (IntentConstant.EXPERT_LIST == mExpertListEntrance) {
            requestParams = NetConstant.getOrganizationExpertListParams(mContext, mCurrentPage, mIdentifyType);
        } else if (IntentConstant.EXPERT_CLASSIFICATION == mExpertListEntrance) {
            if (mOrganizationId > 0) {
                //请求机构专家列表数据
                requestParams = NetConstant.getOrganizationExpertListParams(mContext, mCurrentPage, mOrganizationId, mIdentifyType);
            } else {
                // 请求关注专家列表数据
                requestParams = NetConstant.getAttentionExpertListParams(mContext, mCurrentPage);
            }
        }
        if (requestParams != null) {
            NetUtils.getInstance(false).get(mContext, requestParams, new ExpertListListener(mContext));
        }
    }

    private void attachData(List<ExpertListResponse.DataEntity> list) {
        mRefreshLayout.setRefreshing(false);
        if (mCurrentPage == 1) {
            if (list != null) {
                mExpertAdapter.resetData(list);
            }
        } else {
            if (list != null) {
                mExpertAdapter.addData(list);
            }
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
            default:
                super.onClick(view);
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

    /**
     * 用于判断滑动到底部自动加载更多
     */
    private class ExpertScrollListener extends RecyclerView.OnScrollListener {

        private int lastVisibleItem;
        private LinearLayoutManager manager;

        private ExpertScrollListener(LinearLayoutManager manager) {
            this.manager = manager;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 3 >= mExpertAdapter.getItemCount()) {
                mCurrentPage++;
                attachData();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = manager.findLastVisibleItemPosition();
        }
    }

    /**
     * 请求专家列表数据
     */
    private class ExpertListListener extends NetUtils.Callback<ExpertListResponse> {

        public ExpertListListener(Context context) {
            super(context, ExpertListResponse.class);
        }

        @Override
        public void onNetSuccess(ExpertListResponse response) {
            mList = response.getData();
            attachData(mList);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mList != null) {
                mList.clear();
            }
            attachData(mList);
        }


        @Override
        public void onConvertFailed(String json) {
            mList.clear();
            attachData(mList);
        }
    }
}
