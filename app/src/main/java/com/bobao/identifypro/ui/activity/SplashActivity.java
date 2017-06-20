package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bobao.identifypro.R;
import com.bobao.identifypro.application.IdentifyProApplication;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.PriceQueryResponse;
import com.bobao.identifypro.domain.TPushCustomContent;
import com.bobao.identifypro.manager.IdentifyProUserInfoManager;
import com.bobao.identifypro.service.baidulocation.LocationService;
import com.bobao.identifypro.task.StringToBeanTask;
import com.bobao.identifypro.utils.DeviceUtil;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.LogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity implements StringToBeanTask.ConvertListener<TPushCustomContent> {

    private static final int SleepTime = 2000;

    private Intent mIntent;

    private LocationService locationService;

    private boolean mIsFromNotification;
    private int mPageIndex = -1;
    private String mGoodsId;

    private List<PriceQueryResponse.DataEntity> mQueryList;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        //定点推送传古来的数据
        mIsFromNotification = intent.getBooleanExtra(IntentConstant.IS_FROM_NOTIFICATION, false);
        mPageIndex = intent.getIntExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
    }

    @Override
    protected void initData() {
        mQueryList = new ArrayList<>();
        mIntent = new Intent();
        isNeedDoubleClick = true;
        setSwipeBackEnable(false);

//        MobclickAgent.updateOnlineConfig(this);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_splash_layout;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initContent() {
        // -----------location config ------------
        locationService = ((IdentifyProApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
    }

    @Override
    protected void refreshData() {
        // 判断app打开的方式
        XGPushClickedResult result = XGPushManager.onActivityStarted(mActivity);
        if (result != null) {
            StringToBeanTask<TPushCustomContent> task = new StringToBeanTask<>(TPushCustomContent.class, this);
            task.execute(result.getCustomContent());
            if (DeviceUtil.isApkDebugable(mContext)) {
                LogUtils.d(mContext, TAG, "TPush--", "onResumeXGPushClickedResult:", result);
            }
        } else {
            if (IdentifyProUserInfoManager.getSplashShow(mContext)) {
                new Handler().post(new StartActivity());
            } else {
                mIntent.setClass(mContext, GuidePageActivity.class);
                new Handler().postDelayed(new AutoStart(mIntent), 0);
            }
        }

    }

    @Override
    public void onConvertSuccess(TPushCustomContent response) {
        if (response == null) {
            return;
        }
        Intent intent = new Intent();
        switch (response.getOpen()) {
            case "expert":
                intent.setClass(mContext, ExpertDetailActivity.class);
                intent.putExtra(IntentConstant.EXPERT_ID, response.getId());
                break;
            case "service":
                intent = new Intent(mContext, ServiceNoteActivity.class);
                intent.putExtra(IntentConstant.SERVICE_TYPE, response.getId());
                intent.putExtra(IntentConstant.SERVICE_TYPE_NAME, AppConstant.SERVICE_TYPE_NAME[Integer.valueOf(response.getId()) - 1]);
                jump(intent);
                break;
            case "identify"://未支付
            case "identifying"://进行中的最后一天
            case "comment"://已完成中的评价
                int pageIndex = TextUtils.isEmpty(response.getId()) ? 0 : Integer.valueOf(response.getId());
                intent.setClass(mContext, UserAppointmentExpertsActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, pageIndex);
                mGoodsId = null;
                new Handler().postDelayed(new AutoStart(intent), 500);
                break;
            case "feedback":
                intent.setClass(mContext, FeedBackActivity.class);
                new Handler().postDelayed(new AutoStart(intent), 1000);
                break;
            case "enquiry":
                mGoodsId = response.getId();
                // 请求数据
                NetUtils.getInstance(false).get(mContext, NetConstant.getUserPriceQueryParams(mContext, 0, 1), new PriceQueryListener(mContext));
                break;
            case "html":
            default:
                intent.setClass(SplashActivity.this, MainActivity.class);
                intent.putExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.tv_home);
                break;
        }
        new Handler().postDelayed(new AutoStart(intent), 1000);
    }

    @Override
    public void onConvertFailed(String json) {
        new Handler().postDelayed(new AutoStart(new Intent(mContext, MainActivity.class)), 1000);
    }

    /**
     * 进入Activity
     */
    private class StartActivity implements Runnable {

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            long costTime = System.currentTimeMillis() - start;

            mIntent.setClass(mContext, MainActivity.class);
            mIntent.putExtra(IntentConstant.IS_FROM_NOTIFICATION, mIsFromNotification);
            mIntent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, mPageIndex);
            new Handler().postDelayed(new AutoStart(mIntent), SleepTime - costTime);
        }
    }

    /**
     * 启动Activity
     */
    private class AutoStart implements Runnable {
        private Intent intent;

        private AutoStart(Intent intent) {
            this.intent = intent;
        }

        @Override
        public void run() {
            jump(intent);
            finish();
        }
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    /***
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                UserInfoUtils.saveUserLatitude(mContext, location.getLatitude());
                UserInfoUtils.saveUserLongitude(mContext, location.getLongitude());
                if (location.getLocType() == BDLocation.TypeServerError) {
                    DialogUtils.showShortPromptToast(mContext, R.string.type_server_error_describe);
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    DialogUtils.showShortPromptToast(mContext, R.string.type_net_work_exception_describe);
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    DialogUtils.showShortPromptToast(mContext, R.string.type_criteria_exception_describe);
                }
            }
        }
    };

    private class PriceQueryListener extends NetUtils.Callback<PriceQueryResponse> {
        public PriceQueryListener(Context context) {
            super(context, PriceQueryResponse.class);
        }

        @Override
        public void onNetSuccess(PriceQueryResponse response) {
            if (response == null) {
                return;
            }
            mQueryList = response.getData();
            PriceQueryResponse.DataEntity dataEntity = null;
            for (int i = 0; i < mQueryList.size(); i++) {
                if (mGoodsId.equals(mQueryList.get(i).getId())) {
                    dataEntity = mQueryList.get(i);
                    break;
                }
            }
            if (dataEntity == null) {
                return;
            }
            mGoodsId = null;
            // 将参数写入application
            IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOODS_STATE, dataEntity.getState());
            IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOODS_PRICE, dataEntity.getPrice());
            IdentifyProApplication.setIntentData(IntentConstant.QUERY_REPORT, dataEntity.getReport());
            IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOODS_PHOTO, dataEntity.getPhoto());
            IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOODS_FROM, dataEntity.getFrom());
            IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOODS_TO, dataEntity.getTo());
            IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOODS_ID, dataEntity.getId());
            IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOODS_HEAD, dataEntity.getAsk_head_img());
            IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOOD_NAME, dataEntity.getAsk_user_name());
            IdentifyProApplication.setIntentData(IntentConstant.IS_MY_ORDER_FLAGS, false);
            Intent intent = new Intent(SplashActivity.this, PriceQueryContentActivity.class);
            new Handler().postDelayed(new AutoStart(intent), 500);
        }
    }
}
