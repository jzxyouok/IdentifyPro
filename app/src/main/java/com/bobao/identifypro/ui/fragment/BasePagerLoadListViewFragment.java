package com.bobao.identifypro.ui.fragment;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.lidroid.xutils.http.RequestParams;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;


/**
 * Created by you on 2015/6/2.
 */
public abstract class BasePagerLoadListViewFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {
    protected SwipyRefreshLayout mSwipyRefreshLayout;
    protected BaseAdapter mAdapter;
    protected int mCurrentPage;
    protected ListView mListView;
    protected String mUrl;
    protected View mLoadFailedUI;
    protected View mLoadingUI;
    protected TextView mLoadFailedTipTv;
    protected Button mLoadFailedBtn;

    @Override
    protected void getArgumentsData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_user_sub;
    }

    @Override
    protected void initContent() {
        mLoadingUI = mRootView.findViewById(R.id.vg_loading);
        mLoadingUI.setVisibility(View.GONE);
        mLoadFailedUI = mRootView.findViewById(R.id.vg_no_data);
        mLoadFailedUI.setVisibility(View.GONE);
        mLoadFailedTipTv = (TextView) mLoadFailedUI.findViewById(R.id.tv_no_data_tip);
        mLoadFailedBtn = (Button) mLoadFailedUI.findViewById(R.id.btn_no_data_action);

        mSwipyRefreshLayout = (SwipyRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        mSwipyRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
        mListView = (ListView) mRootView.findViewById(R.id.lv_collections);
        mAdapter = getAdapter();
        mListView.setAdapter(mAdapter);
        startPageLoadRequest(1);
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
        if (swipyRefreshLayoutDirection == SwipyRefreshLayoutDirection.TOP) {
            //下拉刷新
            mCurrentPage = 1;
            startPageLoadRequest(mCurrentPage);
        } else {
            startPageLoadRequest(mCurrentPage + 1);
        }
    }

    protected abstract BaseAdapter getAdapter();

    protected abstract RequestParams configNetRequestParams();

    protected abstract String configUrl();

    protected RequestParams mParams = new RequestParams();

    protected abstract void startNetPagerDataRequest(int pageIndex);

    protected void startPageLoadRequest(final int pageIndex) {
        mParams = configNetRequestParams();
        mParams.addQueryStringParameter("page", String.valueOf(pageIndex));
        mUrl = configUrl();
        startNetPagerDataRequest(pageIndex);
    }

    /**
     * 拿到数据,填充UI
     */
    protected void updateContentUI() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void attachData() {

    }

    protected void handleLoadFailedUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mLoadFailedTipTv.setText(R.string.loading_failed);
        mLoadFailedBtn.setVisibility(View.VISIBLE);
        mLoadFailedBtn.setText(R.string.retry);
        mLoadFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingUI.setVisibility(View.VISIBLE);
                if (!NetUtils.isNetworkConnected(mContext)) {
                    mLoadingUI.setVisibility(View.GONE);
                    DialogUtils.showShortPromptToast(mContext, R.string.cannot_connect_network);
                } else {
                    startPageLoadRequest(1);
                }
            }
        });
    }


    protected void handleNoDataUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mLoadFailedTipTv.setText(R.string.no_more_data);
        mLoadFailedBtn.setVisibility(View.INVISIBLE);
    }
}
