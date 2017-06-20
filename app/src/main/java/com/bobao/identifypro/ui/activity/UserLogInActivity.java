package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.LoginStepResponse;
import com.bobao.identifypro.listener.XGPushCallback;
import com.bobao.identifypro.manager.SocialPlatformLoginManager;
import com.bobao.identifypro.ui.dialog.ProgressDialog;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.lidroid.xutils.exception.HttpException;
import com.tencent.android.tpush.XGPushManager;

public class UserLogInActivity extends BaseActivity {
    private static final int USER_NAME_WATCHER = 1;
    private static final int PASSWORD_WATCHER = 2;
    private EditText mUserNameEt;
    private EditText mUserPasswordEt;
    private TextView mLoginTv;
    private Class<?> mTargetActivity;
    private ProgressDialog mProgressDialog;
    private CheckBox mPswChoice;
    private ImageView mDeletePswView;

    private InputMethodManager mInputMethodManager;

    @Override
    protected void getIntentData() {
//        String targetActivityName = getIntent().getStringExtra(IntentConstant.TARGET_ACTIVITY);
//        if (!TextUtils.isEmpty(targetActivityName)) {
//            switch (targetActivityName) {
//                case "PriceQueryContentActivity":
//                    mTargetActivity = PriceQueryContentActivity.class;
//                    break;
//                case "SubmitOrderActivity":
//                    mTargetActivity = SubmitOrderActivity.class;
//                    break;
//                case "OrderDetailActivity":
//                    mTargetActivity = OrderDetailActivity.class;
//                    break;
//                case "InfoDetailActivity":
//                    mTargetActivity = InfoDetailActivity.class;
//                    break;
//                default:
//                    break;
//            }
//        }
    }

    @Override
    protected void initData() {
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        isNeedDoubleClick = true;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_userlogin;
    }

    @Override
    protected void initTitle() {
        // 返回按钮
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setVisibility(View.GONE);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(R.string.user_login);
        // 注册按钮
        TextView registerView = (TextView) findViewById(R.id.tv_right);
        registerView.setText(R.string.user_register);
        setOnClickListener(registerView);

    }

    @Override
    protected void initContent() {
        mUserNameEt = (EditText) findViewById(R.id.et_username);
        mUserPasswordEt = (EditText) findViewById(R.id.et_userpsw);
        mPswChoice = (CheckBox) findViewById(R.id.psw_choice);
        mDeletePswView = (ImageView) findViewById(R.id.img_delete);
        mLoginTv = (TextView) findViewById(R.id.tv_login);
        View qqLoginView = findViewById(R.id.tv_qq_login);
        View weChatLoginView = findViewById(R.id.tv_wx_login);
        View forgetView = findViewById(R.id.tv_forget_psw);
        setOnClickListener(mLoginTv, qqLoginView, weChatLoginView, forgetView, mDeletePswView);
        mPswChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mUserPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mUserPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                CharSequence mPhoneEtText = mUserPasswordEt.getText();
                if (mPhoneEtText != null) {
                    Spannable spanText = (Spannable) mPhoneEtText;
                    Selection.setSelection(spanText, mPhoneEtText.length());
                }
            }
        });

        // 如果用户名改变，清空密码
        mUserNameEt.addTextChangedListener(new Watcher(USER_NAME_WATCHER));
        mUserPasswordEt.addTextChangedListener(new Watcher(PASSWORD_WATCHER));
    }

    @Override
    protected void initFooter() {
    }

    @Override
    protected void attachData() {
    }

    @Override
    protected void refreshData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                Intent intent = new Intent(mContext, UserRegisterActivity.class);
                if (mTargetActivity != null) {
                    intent.putExtra(IntentConstant.TARGET_ACTIVITY, mTargetActivity.getSimpleName());
                }
                UmengUtils.onEvent(mContext, EventEnum.User_LoginPage_Regist_Onclick);
                jump(intent);
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_qq_login:
                SocialPlatformLoginManager.getsInstance(mContext).tencentAuthAndLogIn(mActivity, mTargetActivity);
                UmengUtils.onEvent(mContext, EventEnum.UserQQLogIn);
                break;
            case R.id.tv_wx_login:
                SocialPlatformLoginManager.getsInstance(mContext).weixinAuthAndLogIn(mActivity,mTargetActivity);
                UmengUtils.onEvent(mContext, EventEnum.UserWXLogIn);
                break;
            case R.id.tv_forget_psw:
                jump(mContext, ForgetPswActivity.class);
                UmengUtils.onEvent(mContext, EventEnum.ForgetPsw);
                break;
            case R.id.img_delete:
                mUserPasswordEt.setText("");
                mInputMethodManager.showSoftInput(mUserPasswordEt, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        String name = mUserNameEt.getText().toString().trim();
        String password = mUserPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_empty_number);
        } else if (!StringUtils.checkPhoneNumber(name)) {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        } else if (TextUtils.isEmpty(password)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_empty_password);
        } else if (password.length() < IntentConstant.PASSWORD_MIN_LENGTH) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_password_too_short);
        } else if (password.length() > IntentConstant.PASSWORD_MAX_LENGTH) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_password_too_long);
        } else {
            // 登录
            mProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.is_login));
            NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getLoginParams(mContext, name, password), new LoginListener(mContext));
            UmengUtils.onEvent(mContext, EventEnum.UserLogIn);
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

    private class LoginListener extends NetUtils.Callback<LoginStepResponse> {

        public LoginListener(Context context) {
            super(context, LoginStepResponse.class);
        }

        @Override
        public void onNetSuccess(LoginStepResponse response) {
            // 登录
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (response == null || response.getData() == null) {
                if (response != null) {
                    DialogUtils.showShortPromptToast(UserLogInActivity.this, response.getMessage());
                }
                return;
            }
            DialogUtils.showShortPromptToast(mContext, R.string.login_success);
            Intent intent = new Intent();
            //保存信息到本地
            UserInfoUtils.saveUserLoginInfo(mContext, response.getData());
            UserInfoUtils.saveCacheHeadImagePath(mContext, response.getData().getHeadimg());
            UserInfoUtils.setSocialLoginFlg(mContext, false);
            //登陆成功后，刷新信鸽推送的注册名
            XGPushManager.registerPush(mContext, StringUtils.getString("JQ", UserInfoUtils.getUserId(mContext)), new XGPushCallback(mContext, TAG));
            if (mTargetActivity != null) {
                intent.setClass(mContext, mTargetActivity);
                intent.putExtra(IntentConstant.IntentAction, IntentConstant.SubmitOrder);
            } else {
                //跳转回到主页----"首页"
                intent.setClass(mContext, MainActivity.class);
                intent.putExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.tv_user);
            }
            jump(intent);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.login_failed);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }

        @Override
        public void onConvertFailed(String json) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            DialogUtils.showShortPromptToast(mContext, R.string.login_failed);
        }
    }

    private class Watcher implements TextWatcher {
        private int mWatcherFlag;

        public Watcher(int watcherFlag) {
            this.mWatcherFlag = watcherFlag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (USER_NAME_WATCHER == mWatcherFlag) {
                mUserPasswordEt.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (PASSWORD_WATCHER == mWatcherFlag) {
                if (TextUtils.isEmpty(s.toString())) {
                    mDeletePswView.setVisibility(View.GONE);
                } else {
                    mDeletePswView.setVisibility(View.VISIBLE);
                }
            }

            if (mUserNameEt.length() == IntentConstant.PHONE_LENGTH && mUserPasswordEt.length() >= IntentConstant.PASSWORD_MIN_LENGTH &&
                    mUserPasswordEt.length() <= IntentConstant.PASSWORD_MAX_LENGTH) {
                mLoginTv.setBackgroundResource(R.drawable.bg_button_corner_4_orange_yellow);
                mLoginTv.setEnabled(true);
            } else {
                mLoginTv.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mLoginTv.setEnabled(false);
            }
        }
    }
}
