package com.bobao.identifypro.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.domain.UserInfoResponse;
import com.bobao.identifypro.ui.activity.ExpertListActivity;
import com.bobao.identifypro.ui.activity.FeedBackActivity;
import com.bobao.identifypro.ui.activity.SettingActivity;
import com.bobao.identifypro.ui.activity.UserAppointmentExpertsActivity;
import com.bobao.identifypro.ui.activity.UserLogInActivity;
import com.bobao.identifypro.ui.activity.UserPrivateInfoActivity;
import com.bobao.identifypro.ui.activity.UserWalletActivity;
import com.bobao.identifypro.ui.activity.WebViewActivity;
import com.bobao.identifypro.ui.dialog.CommonDialog;
import com.bobao.identifypro.ui.dialog.NetworkUnConnectedDialog;
import com.bobao.identifypro.utils.AppUtils;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.SharedPreferencesUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UriUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.exception.HttpException;

import java.util.HashMap;

/**
 * Created by star on 15/5/29.
 */
public class UserFragment extends BaseFragment {
    private static final int NO_LOGIN = 0;
    private static final int LOAD_SUCCESS = 1;
    private static final int LOAD_FAIL = 2;

    private SimpleDraweeView mPortraitView;
    private TextView mNickNameTv;

    private TextView mBubbleTvPayment;
    private TextView mBubbleTvIdentify;
    private TextView mBubbleTvIdentified;

    private View mIdentifyNoPayView;
    private View mIdentifyNoIdentifyView;
    private View mIdentifyPaidView;

    private TextView mBubbleTvFeedback;

    private CommonDialog mCommonDialog;

    private String mUserId;

    private int mShowDialogFlag;

    private NetworkUnConnectedDialog mNetworkUnConnectedDialog;

    private View.OnClickListener mTryAgainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mNetworkUnConnectedDialog != null) {
                mNetworkUnConnectedDialog.dismiss();
            }
            startUserInfoRequest();
        }
    };

    private ShowDialogHandler showDialogHandler = new ShowDialogHandler();

    private class ShowDialogHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NO_LOGIN:
                    showPickDialog(R.string.not_login, R.string.go_to_login_or_register);
                    break;
                case LOAD_FAIL:
                    showPickDialog(R.string.loading_failed, R.string.confirm_loading);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private class DialogShow implements Runnable {
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = mShowDialogFlag;
            showDialogHandler.sendMessage(msg);
        }
    }

    private View.OnClickListener mCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCommonDialog != null) {
                mCommonDialog.dismiss();
            }
        }
    };
    private View.OnClickListener mSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContext != null) {
                if (mShowDialogFlag == NO_LOGIN) {
                    Intent intent = new Intent(mContext, UserLogInActivity.class);
                    jump(intent);
                } else {
                    if (!NetUtils.isNetworkConnected(mContext)) {
                        DialogUtils.showShortPromptToast(mContext, R.string.cannot_connect_network);
                    } else {
                        startUserInfoRequest();
                    }
                }
                if (mCommonDialog != null) {
                    mCommonDialog.dismiss();
                }
            }
        }
    };

    @Override
    protected void getArgumentsData() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initContent() {
        mPortraitView = (SimpleDraweeView) mRootView.findViewById(R.id.img_head);
        mNickNameTv = (TextView) mRootView.findViewById(R.id.tv_nick_name);

        mBubbleTvPayment = (TextView) mRootView.findViewById(R.id.tv_bubble_payment);
        mBubbleTvIdentify = (TextView) mRootView.findViewById(R.id.tv_bubble_identity);
        mBubbleTvIdentified = (TextView) mRootView.findViewById(R.id.tv_bubble_identified);
        mBubbleTvFeedback = (TextView) mRootView.findViewById(R.id.tv_bubble_suggestion);

        mIdentifyNoPayView = mRootView.findViewById(R.id.ll_identify_no_pay);
        mIdentifyNoIdentifyView = mRootView.findViewById(R.id.ll_identify_no_identify);
        mIdentifyPaidView = mRootView.findViewById(R.id.ll_identify_identified);

        View mUserFocusView = mRootView.findViewById(R.id.rl_user_focus);
        View mUserWalletView = mRootView.findViewById(R.id.rl_user_wallet);
        View mUserSettingView = mRootView.findViewById(R.id.rl_user_setting);
        View mUserHelpView = mRootView.findViewById(R.id.rl_user_help);
        View mUserFeedBackView = mRootView.findViewById(R.id.rl_user_suggestion);

        setOnClickListener(mPortraitView, mIdentifyNoPayView, mIdentifyNoIdentifyView, mIdentifyPaidView, mUserFocusView, mUserWalletView, mUserSettingView,
                mUserHelpView, mUserFeedBackView);
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onClick(View view) {
        if (mContext == null) {
            return;
        }
        Intent intent;
        //用户未登录，则在点击事件中直接跳转登录界面
        if (!UserInfoUtils.checkUserLogin(mContext)) {
            mShowDialogFlag = NO_LOGIN;
            new Thread(new DialogShow()).start();
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.img_head://修改用户头
                jump(mContext, UserPrivateInfoActivity.class);
                UmengUtils.onEvent(mContext, EventEnum.UserPageChangeInfoClick);
                break;
            case R.id.ll_identify_no_pay:
                intent = new Intent(mContext, UserAppointmentExpertsActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
                getActivity().startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageIdentifyNoPayClick, map);
                break;
            case R.id.ll_identify_no_identify:
                intent = new Intent(mContext, UserAppointmentExpertsActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_IDENTIFY);
                getActivity().startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageIdentifyNoidentifyClick, map);
                break;
            case R.id.ll_identify_identified:
                intent = new Intent(mContext, UserAppointmentExpertsActivity.class);
                intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_IDENTIFIED);
                getActivity().startActivity(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageIdentifiedClick, map);
                break;
            case R.id.rl_user_focus:
                intent = new Intent(mContext, ExpertListActivity.class);
                intent.putExtra(IntentConstant.EXPERT_LIST_ENTRANCE, IntentConstant.EXPERT_CLASSIFICATION);
                intent.putExtra(IntentConstant.ORGANIZATION_NAME, R.string.attention);
                jump(intent);
                break;
            case R.id.rl_user_wallet:
                intent = new Intent(mContext, UserWalletActivity.class);
                intent.putExtra(IntentConstant.USER_ID, mUserId);
                jump(intent);
                break;
            case R.id.rl_user_setting:
                jump(mContext, SettingActivity.class);
                break;
            case R.id.rl_user_help:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(IntentConstant.WEB_URL, NetConstant.IDENTIFY_FAQ);
                intent.putExtra(IntentConstant.WEB_TITLE, getString(R.string.help));
                jump(intent);
                UmengUtils.onEvent(mContext, EventEnum.UserProblems);
                break;
            case R.id.rl_user_suggestion:
                jump(mContext, FeedBackActivity.class);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(mContext, EventEnum.UserPageFeedBackClick, map);
                break;
            default:
                super.onClick(view);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mContext == null) {
            return;
        }
        if (!NetUtils.isNetworkConnected(mContext) && mNetworkUnConnectedDialog == null) {
            mNetworkUnConnectedDialog = DialogUtils.showNetworkUnConnectedDialog(mContext, mTryAgainClickListener);
        }
        if (UserInfoUtils.checkUserLogin(mContext)) {
            //用户登录后 头像可能随时改动,需要刷新
            mIdentifyNoIdentifyView.setSelected(false);
            mIdentifyNoPayView.setSelected(false);
            mIdentifyPaidView.setSelected(false);
            mBubbleTvPayment.setVisibility(View.GONE);//先隐藏掉气泡
            mBubbleTvIdentify.setVisibility(View.GONE);
            mBubbleTvFeedback.setVisibility(View.GONE);
            mBubbleTvIdentified.setVisibility(View.GONE);
            startUserInfoRequest();
        } else {
            mNickNameTv.setText(R.string.no_registered_account);
            mPortraitView.setImageURI(UriUtils.getResourceUri(mContext, R.drawable.icon_user_default));

            mBubbleTvPayment.setVisibility(View.GONE);
            mBubbleTvIdentify.setVisibility(View.GONE);
            mBubbleTvFeedback.setVisibility(View.GONE);
            mBubbleTvIdentified.setVisibility(View.GONE);
        }
    }

    private void startUserInfoRequest() {
        NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getUserInfoParams(mContext), new UserInfoListener(mContext));
    }

    /**
     * 选择提示对话框
     */
    private void showPickDialog(int title, int content) {
        if (mContext != null) {
            mCommonDialog = DialogUtils.showCommonDialog(mContext, getString(title), getString(content), getString(R.string.negative), getString(R.string.positive), mSubmitListener, mCancelListener);
        }
    }

    private class UserInfoListener extends NetUtils.Callback<UserInfoResponse> {

        public UserInfoListener(Context context) {
            super(context, UserInfoResponse.class);
        }

        @Override
        public void onConvertFailed(String json) {
            if (mContext != null) {
                mNickNameTv.setText(UserInfoUtils.getUserNickName(mContext));
            }
        }

        @Override
        public void onNetSuccess(UserInfoResponse data) {
            if (data != null && mContext != null) {
                mShowDialogFlag = LOAD_SUCCESS;
                new Thread(new DialogShow()).start();
                UserInfoResponse.DataEntity.UserDataEntity user = data.getData().getUser_data();
                UserInfoResponse.DataEntity.ProGoodsEntity cost = data.getData().getPro_goods();
                if (!TextUtils.isEmpty(user.getHead_img())) {
                    mPortraitView.setImageURI(Uri.parse(user.getHead_img()));
                    UserInfoUtils.saveCacheHeadImagePath(mContext, user.getHead_img());
                } else {
                    mPortraitView.setImageURI(UriUtils.getResourceUri(mContext, R.drawable.icon_user_default));
                }
                String userName = TextUtils.isEmpty(user.getNikename()) ? user.getUser_name() : user.getNikename();
                if (userName.length() > 7) {
                    userName = StringUtils.getString(userName.substring(0, 6), "...");
                }
                mNickNameTv.setText(userName);

                mUserId = user.getUser_id();
                int noChargNum = 0;
                try {
                    noChargNum = Integer.parseInt(cost.getWait());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                SharedPreferencesUtils.setSharedPreferencesBoolean(mContext, AppConstant.HAS_NOCHARGE_IDENTIFY, noChargNum > 0);
                showBubble(cost.getWait(), mBubbleTvPayment);
                showBubble(cost.getConduct(), mBubbleTvIdentify);
                showBubble(cost.getComplete(), mBubbleTvIdentified);
                showBubble(data.getData().getFeedback(), mBubbleTvFeedback);

                int localVersionCode = AppUtils.getVersionCode(mContext);
                int latestVersionCode = !TextUtils.isEmpty(data.getData().getVerCode()) ? Integer.valueOf(data.getData().getVerCode()) : 0;
                if (localVersionCode < latestVersionCode) {
                    UmengUtils.onEvent(mContext, EventEnum.User_Setting_Update_HaveUpdate);
                    UserInfoUtils.saveAppIsUpdate(mContext, true);
                } else {
                    UserInfoUtils.saveAppIsUpdate(mContext, false);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mContext != null) {
                mNickNameTv.setText(UserInfoUtils.getUserNickName(mContext));
            }
        }
    }

    private void showBubble(String noCharg, TextView noChargeTv) {
        if (TextUtils.isEmpty(noCharg)) {
            noChargeTv.setVisibility(View.GONE);
            return;
        }
        int noChargeCount = Integer.valueOf(noCharg);
        if (noChargeCount > 0) {
            if (noChargeCount > 99) {
                noCharg = String.valueOf(99);
            }
            noChargeTv.setText(noCharg);
            noChargeTv.setVisibility(View.VISIBLE);
        } else {
            noChargeTv.setVisibility(View.GONE);
        }
    }
}
