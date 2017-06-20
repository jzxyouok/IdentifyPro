package com.bobao.identifypro.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.constant.NetWorkRequestConstants;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.domain.AttentionCollectionResponse;
import com.bobao.identifypro.domain.ExpertDetailResponse;
import com.bobao.identifypro.domain.ExpertServiceInfoResponse;
import com.bobao.identifypro.manager.UMengShareManager;
import com.bobao.identifypro.ui.adapter.AlbumAdapter;
import com.bobao.identifypro.ui.adapter.ExpertDetailCommentAdapter;
import com.bobao.identifypro.ui.adapter.ExpertDetailServiceAdapter;
import com.bobao.identifypro.ui.popupwindow.CustomShareBoard;
import com.bobao.identifypro.ui.popupwindow.ServiceAppointmentPopupWindow;
import com.bobao.identifypro.ui.widget.fix.FixedListView;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.AnimatorUtils;
import com.bobao.identifypro.utils.BitmapUtils;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by you on 2015/6/5.
 */
public class ExpertDetailActivity extends BaseActivity {

    private SimpleDraweeView mPortraitView;
    private TextView mNameView;
    private TextView mDescView;
    private TextView mIdentifyNumView;
    private TextView mFansNumView;
    private TextView mAppreciateNumView;
    private TextView mGoodAtView;
    private TextView mLevelView;
    private TextView mAssistView;
    private TextView mIntroductionView;
    private View mAlbumContainerView;
    private RecyclerView mAlbumView;
    private TextView mSubmitView;
    private View mShareContentView;

    private LinearLayout mOrdinaryServiceLL;
    private LinearLayout mSpecialServiceLL;
    private RecyclerView mOrdinaryServiceRecyclerView;
    private RecyclerView mSpecialServiceRecyclerView;

    private AlbumAdapter mAlbumAdapter;
    private ExpertDetailServiceAdapter mExpertDetailOrdinaryServiceAdapter;
    private ExpertDetailServiceAdapter mExpertDetailSpecialServiceAdapter;

    private String mExpertId;
    private String mExpertName;
    private String mIdentifyType;
    private String mIdentifyTypeName;

    private ArrayList<String> mSerivceTypeId;
    private ArrayList<String> mSerivceTypeName;
    private ArrayList<String> mServiceTypePrice;
    private ArrayList<String> mServiceTypeInfo;

    private String mImageFileAbsPath;
    private CustomShareBoard mShareBoard;
    private FrameLayout mCollectRelativeLayout;
    private ImageView mCollectImageView;
    private ImageView mAttentionAnimatorView;
    private boolean mCollectState;
    private LinearLayout mllScroll;

    private String mIdentifyMethodSwitchInfos;//记录该专家是否开通了相应的鉴定方式
    private String mIdentifyMethodPrices;//记录该专家鉴定方式的价格

    private ImageView mShareView;
    private TextView mWarmPrompt;

    private final String CAN_NOT_APPOINTMENT = "0";

    private TextView mRightView;//收藏

    private FixedListView mCommentListView;
    private ExpertDetailCommentAdapter mCommentAdapter;
    private List<ExpertDetailResponse.DataEntity.KindArrEntity> mIdentifyItemList = new ArrayList<>();
    private String strItems;

    private TextView mNoEvaluateTv;

    private ServiceAppointmentPopupWindow mServiceAppointmentPopupWindow;
    private int mServiceTypePosition;
    private List<ExpertServiceInfoResponse.DataEntity> mServiceTypeList;

    @Override
    protected void getIntentData() {
        mExpertId = getIntent().getStringExtra(IntentConstant.EXPERT_ID);
        mIdentifyType = getIntent().getStringExtra(IntentConstant.IDENTIFY_TYPE);
        mIdentifyTypeName = getIntent().getStringExtra(IntentConstant.IDENTIFY_TYPE_NAME);
    }

    @Override
    protected void initData() {
        mAlbumAdapter = new AlbumAdapter();
        mCommentAdapter = new ExpertDetailCommentAdapter(mContext);
        mExpertDetailOrdinaryServiceAdapter = new ExpertDetailServiceAdapter(mContext, IntentConstant.EXPERTS_DETAIL_ORDINARY_SERVICE);
        mExpertDetailSpecialServiceAdapter = new ExpertDetailServiceAdapter(mContext, IntentConstant.EXPERTS_DETAIL_SPECIAL_SERVICE);
        mServiceTypePosition = 0;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_expert_detail;
    }

    @Override
    protected void initTitle() {
        // 设置背景色为透明
        View titleView = findViewById(R.id.layout_title);
        titleView.setBackgroundColor(Color.TRANSPARENT);
        // 设置返回按钮
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText("");

        mRightView = (TextView) findViewById(R.id.tv_right);

        setOnClickListener(backView, mRightView);
    }

    @Override
    protected void initContent() {
        mOrdinaryServiceLL = (LinearLayout) findViewById(R.id.ll_ordinary);
        mSpecialServiceLL = (LinearLayout) findViewById(R.id.ll_special);
        mOrdinaryServiceRecyclerView = (RecyclerView) findViewById(R.id.rcv_ordinary_service);
        LinearLayoutManager ordinaryManager = new LinearLayoutManager(mContext);
        ordinaryManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mOrdinaryServiceRecyclerView.setLayoutManager(ordinaryManager);
        mSpecialServiceRecyclerView = (RecyclerView) findViewById(R.id.rcv_special_service);
        LinearLayoutManager specialManager = new LinearLayoutManager(mContext);
        specialManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSpecialServiceRecyclerView.setLayoutManager(specialManager);

        mCommentListView = (FixedListView) findViewById(R.id.flv_comment);
        mNoEvaluateTv = (TextView) findViewById(R.id.no_evaluate_tv);
        mWarmPrompt = (TextView) findViewById(R.id.warm_prompt_online_tv);
        mShareView = (ImageView) findViewById(R.id.img_share);
        mPortraitView = (SimpleDraweeView) findViewById(R.id.sdv_portrait);
        mNameView = (TextView) findViewById(R.id.tv_name);
        mDescView = (TextView) findViewById(R.id.tv_desc);
        mIdentifyNumView = (TextView) findViewById(R.id.tv_identify);
        mFansNumView = (TextView) findViewById(R.id.tv_fans);
        mAppreciateNumView = (TextView) findViewById(R.id.tv_appreciate);
        mGoodAtView = (TextView) findViewById(R.id.tv_good_at);
        mLevelView = (TextView) findViewById(R.id.tv_level);
        mAssistView = (TextView) findViewById(R.id.tv_assist);
        mIntroductionView = (TextView) findViewById(R.id.tv_introduction);
        // 人群和场景
        mAlbumContainerView = findViewById(R.id.ll_album);
        mAlbumView = (RecyclerView) findViewById(R.id.rv_album);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAlbumView.setLayoutManager(manager);
        // 提交
        mSubmitView = (TextView) findViewById(R.id.tv_submit);
        mShareContentView = findViewById(R.id.share_content);
        mllScroll = (LinearLayout) findViewById(R.id.ll_scroll);
        //关注
        mCollectRelativeLayout = (FrameLayout) findViewById(R.id.rl_collect);
        mCollectImageView = (ImageView) findViewById(R.id.img_animator);
        mAttentionAnimatorView = (ImageView) findViewById(R.id.img_animator_attention);
        mAttentionAnimatorView.setVisibility(View.GONE);
        // 相册
        mAlbumView.setAdapter(mAlbumAdapter);
        //评论
        mCommentListView.setAdapter(mCommentAdapter);
        //服务类型
        mOrdinaryServiceRecyclerView.setAdapter(mExpertDetailOrdinaryServiceAdapter);
        mSpecialServiceRecyclerView.setAdapter(mExpertDetailSpecialServiceAdapter);
        //分享
        UMengShareManager.getInstance(mContext).setOnShareCompleteListener(new UMengShareManager.OnShareCompleteLisnener() {
            @Override
            public void onShareComplete() {
//                NetUtils.getInstance(false)
//                        .get(mContext, NetConstant.getScoreParams(mContext, NetWorkRequestConstants.GET_SCORE_METHOD_SHARE, UMengShareManager.TYPE_SHARE_EXPERT, mExpertId), null);
                new HttpUtils().send(
                        HttpRequest.HttpMethod.GET,
                        NetConstant.HOST,
                        NetConstant.getScoreParams(mContext, NetWorkRequestConstants.GET_SCORE_METHOD_SHARE, UMengShareManager.TYPE_SHARE_EXPERT, mExpertId), null);
            }
        });
        setOnClickListener(mCollectRelativeLayout, mShareView, mSubmitView, mAssistView);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        // 请求专家详情数据
        NetUtils.getInstance(false).get(mContext, NetConstant.getExpertDetailParams(mContext, mExpertId), new ExpertDetailListener(mContext));
    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        HashMap<String, String> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.tv_submit:
                NetUtils.getInstance(false).get(mContext, NetConstant.getExpertServiceInfoParams(mContext, mExpertId), new ExpertServiceTypeListener(mContext));
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_assist:
                ActivityUtils.makeCallIntent(mContext, mAssistView.getText().toString().trim());
                break;
            case R.id.img_share:
                showShareBoard();
                HashMap<String, String> unengMap = new HashMap<>();
                unengMap.put(UmengConstants.KEY_SHARE_CONTENT_ID, mExpertId);
                UmengUtils.onEvent(this, EventEnum.ShareExpertEntry, unengMap);
                break;
            case R.id.rl_collect:
                attentionExpert();
                HashMap<String, String> attentionMap = new HashMap<>();
                attentionMap.put(UmengConstants.KEY_ATTENTION_EXPERT_ID, mExpertId);
                UmengUtils.onEvent(this, EventEnum.AttentionExpertEntry, attentionMap);
                break;
            case R.id.tv_finish:
                int position = (int) view.getTag();
                mServiceTypePosition = position;
                intent = new Intent(mContext, ServiceAppointmentActivity.class);
                intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_EXPERT_DETAIL_ACTIVITY);//专家详情页，确定专家
                intent.putExtra(IntentConstant.EXPERT_ID, mExpertId);
                intent.putExtra(IntentConstant.EXPERT_NAME, mExpertName);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, mIdentifyType);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, mIdentifyTypeName);
                intent.putExtra(IntentConstant.SERVICE_TYPE, String.valueOf(mServiceTypeList.get(position).getId()));
                intent.putExtra(IntentConstant.SERVICE_TYPE_NAME, mServiceTypeList.get(position).getName());
                intent.putExtra(IntentConstant.APPRAISAL_COST,
                        StringUtils.getString(mServiceTypeList.get(position).getPrice(), mContext.getString(R.string.unit_of_money), mServiceTypeList.get(position).getUnit()));

                if (mSerivceTypeId != null) {
                    intent.putExtra(IntentConstant.SERVICE_TYPE_ID_LIST, mSerivceTypeId);
                    intent.putExtra(IntentConstant.SERVICE_TYPE_NAME_LIST, mSerivceTypeName);
                    intent.putExtra(IntentConstant.SERVICE_TYPE_PRICE_LIST, mServiceTypePrice);
                    intent.putExtra(IntentConstant.SERVICE_TYPE_INFO_LIST, mServiceTypeInfo);
                }
                ActivityUtils.jump(mContext, intent);
                if (mServiceAppointmentPopupWindow != null) {
                    mServiceAppointmentPopupWindow.dismiss();
                }
                break;
            default:
                super.onClick(view);
                break;
        }
    }


    private String[] getItemArr() {
        strItems = null;
        for (int i = 0; i < mIdentifyItemList.size(); i++) {
            strItems = (strItems == null ? StringUtils.getString(mIdentifyItemList.get(i).getName()) :
                    StringUtils.getString(strItems, ",", mIdentifyItemList.get(i).getName()));
        }
        return strItems.split(",");
    }

    private void attentionExpert() {
        if (!UserInfoUtils.checkUserLogin(this)) {
            jump(new Intent(mContext, UserLogInActivity.class));
            DialogUtils.showShortPromptToast(mContext, R.string.not_login);
            return;
        }
        NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getExpertAttentionParams(this, mExpertId), new ExpertAttentionListener(mContext));
    }

    /**
     * 资讯收藏
     */
    private class ExpertAttentionListener extends NetUtils.Callback<AttentionCollectionResponse> {

        public ExpertAttentionListener(Context context) {
            super(context, AttentionCollectionResponse.class);
        }

        @Override
        public void onNetSuccess(AttentionCollectionResponse response) {
            DialogUtils.showShortPromptToast(mContext, response.getData().getData());
            mCollectState = response.getData().isCollect();
            mAttentionAnimatorView.setVisibility(View.VISIBLE);
            AnimatorUtils.startStarAnimator(mAttentionAnimatorView, 600, mCollectState, mCollectionAnimatorListenerAdapter);
            if (mShareBoard != null) {
                mShareBoard.setmIsCollected(mCollectState);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, StringUtils.getString(getString(R.string.attention_failed), s));
        }
    }

    private AnimatorListenerAdapter mCollectionAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            mAttentionAnimatorView.setVisibility(mCollectState ? View.VISIBLE : View.GONE);
            mCollectImageView.setVisibility(mCollectState ? View.VISIBLE : View.GONE);
        }
    };

    private void showShareBoard() {
        if (!UserInfoUtils.checkUserLogin(this)) {
            jump(new Intent(mContext, UserLogInActivity.class));
            DialogUtils.showShortPromptToast(mContext, R.string.not_login);
            return;
        }
        mShareBoard = new CustomShareBoard(this);
        mShareBoard.setId(mExpertId);
        mShareBoard.setShareType(UmengConstants.SHARE_TYPE_EXPERT);
        mShareBoard.setCollectListener(this);
        mShareBoard.initmIsCollected(mCollectState);
        if (TextUtils.isEmpty(mImageFileAbsPath)) {
            mImageFileAbsPath = BitmapUtils
                    .saveBitmap(this, BitmapUtils.convertViewToBitmap(mShareContentView), StringUtils.getString(UserInfoUtils.getUserId(mContext), "expert_share", ".png"));
        }
        String introduce = mIntroductionView.getText().toString().trim();
        mShareBoard.setShareContent(
                StringUtils.getString(
                        getString(R.string.expert_share_title_prefix),
                        mNameView.getText().toString().trim(),
                        ": ",
                        introduce),
                mImageFileAbsPath, StringUtils.getString(UmengConstants.BASE_SHARE_EXPERT_URL, mExpertId));
        mShareBoard.applayBlur();
        mShareBoard.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void finish() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getRunningTasks(1).get(0).numActivities < 2) {
            jump(mContext, MainActivity.class);
        }
        super.finish();
    }

    private void attachData(ExpertDetailResponse.DataEntity data) {
        mExpertName = data.getName();
        mIdentifyItemList.addAll(data.getKindArr());
        mCommentAdapter.setData(data.getCommentArr());
        mNoEvaluateTv.setVisibility(data.getCommentArr().size() == 0 ? View.VISIBLE : View.GONE);
        mPortraitView.setImageURI(Uri.parse(data.getHead_img()));
        mNameView.setText(mExpertName);
        mDescView.setText(data.getHonors());
        mIdentifyNumView.setText(String.format(getString(R.string.indicator_num), data.getJbcount()));
        mFansNumView.setText(String.format(getString(R.string.fans_num), data.getGzcount()));
        mAppreciateNumView.setText(String.format(getString(R.string.appreciate_num), data.getComment()));
        mGoodAtView.setText(data.getKind());
        if (mIdentifyItemList != null) {
            mIdentifyType = mIdentifyItemList.get(0).getId();
            mIdentifyTypeName = mIdentifyItemList.get(0).getName();
        }
//        mDefaultIdentifyType = getDefaultIdentifyType(data.getKind());
        mLevelView.setText(data.getStar_level());
        mAssistView.setText(data.getZhuli());
        mIntroductionView.setText(data.getIntro());

        if (data.getServer() != null) {
            if (data.getServer().getOrdinary() != null && data.getServer().getOrdinary().size() > 0) {
                mOrdinaryServiceLL.setVisibility(View.VISIBLE);
                mExpertDetailOrdinaryServiceAdapter.setIntentData(mIdentifyType, mIdentifyTypeName, mExpertId, mExpertName);
                mExpertDetailOrdinaryServiceAdapter.resetData(data.getServer());
            } else {
                mOrdinaryServiceLL.setVisibility(View.GONE);
            }
            if (data.getServer().getSpecial() != null && data.getServer().getSpecial().size() > 0) {
                mSpecialServiceLL.setVisibility(View.VISIBLE);
                mExpertDetailSpecialServiceAdapter.setIntentData(mIdentifyType, mIdentifyTypeName, mExpertId, mExpertName);
                mExpertDetailSpecialServiceAdapter.resetData(data.getServer());
            } else {
                mSpecialServiceLL.setVisibility(View.GONE);
            }
        }
        if (data.getPhoto().size() > 0) {
            mAlbumContainerView.setVisibility(View.VISIBLE);
            mAlbumAdapter.setData(data.getPhoto());
            mAlbumAdapter.notifyDataSetChanged();
        } else {
            mAlbumContainerView.setVisibility(View.GONE);
        }
        mCollectState = "1".equals(data.getIsfans());
        mAttentionAnimatorView.setVisibility(mCollectState ? View.VISIBLE : View.GONE);
        mCollectImageView.setVisibility(mCollectState ? View.VISIBLE : View.GONE);

        mSubmitView.setVisibility(CAN_NOT_APPOINTMENT.equals(data.getJbapp_yy()) ? View.GONE : View.VISIBLE);
        mSubmitView.setText(R.string.appointment);
    }

    /**
     * 请求专家详情数据
     */
    private class ExpertDetailListener extends NetUtils.Callback<ExpertDetailResponse> {
        public ExpertDetailListener(Context context) {
            super(context, ExpertDetailResponse.class);
        }

        @Override
        public void onNetSuccess(ExpertDetailResponse response) {
            getIdentifyMethodAndPriceInfo(response);
            attachData(response.getData());
        }
    }

    /**
     * 抽取专家支持的鉴定方式及价格
     */
    private void getIdentifyMethodAndPriceInfo(ExpertDetailResponse expertDetail) {
        //Jbapp_pt等鉴定方式，0代表未开通，1代表已开通
        ExpertDetailResponse.DataEntity data = expertDetail.getData();
        mIdentifyMethodSwitchInfos = StringUtils.getString(data.getJbapp_pt(), ",", data.getJbapp_js(), ",", data.getJbapp_sp(), ",", data.getJbapp_yy());
        mIdentifyMethodPrices = StringUtils.getString(data.getPt_price(), ",", data.getJs_price(), ",", data.getSp_price(), ",", data.getYy_price());
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

    /**
     * 请求改专家的服务信息
     */
    private class ExpertServiceTypeListener extends NetUtils.Callback<ExpertServiceInfoResponse> {

        public ExpertServiceTypeListener(Context context) {
            super(context, ExpertServiceInfoResponse.class);
        }

        @Override
        public void onNetSuccess(ExpertServiceInfoResponse response) {
            if (response != null) {
                mServiceTypeList = response.getData();
                mServiceAppointmentPopupWindow = new ServiceAppointmentPopupWindow(ExpertDetailActivity.this, response, ExpertDetailActivity.this, mServiceTypePosition);
                mServiceAppointmentPopupWindow.showAtLocation(ExpertDetailActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                mSerivceTypeId = new ArrayList<>();
                mSerivceTypeName = new ArrayList<>();
                mServiceTypePrice = new ArrayList<>();
                mServiceTypeInfo = new ArrayList<>();
                for (ExpertServiceInfoResponse.DataEntity dataEntity : mServiceTypeList) {
                    mSerivceTypeId.add(String.valueOf(dataEntity.getId()));
                    mSerivceTypeName.add(dataEntity.getName());
                    mServiceTypePrice.add(StringUtils.getString(dataEntity.getPrice(), mContext.getString(R.string.unit_of_money), dataEntity.getUnit()));
                    mServiceTypeInfo
                            .add(StringUtils.getString(dataEntity.getName(), "    ", dataEntity.getPrice(), mContext.getString(R.string.unit_of_money), dataEntity.getUnit()));
                }
            }
        }
    }
}
