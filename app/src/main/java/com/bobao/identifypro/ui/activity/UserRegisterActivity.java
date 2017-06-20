package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.AuthCodeResponse;
import com.bobao.identifypro.domain.LoginStepResponse;
import com.bobao.identifypro.domain.RegisterResponse;
import com.bobao.identifypro.listener.XGPushCallback;
import com.bobao.identifypro.receiver.SMSBroadcastReceiver;
import com.bobao.identifypro.ui.dialog.ProgressDialog;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.lidroid.xutils.exception.HttpException;
import com.tencent.android.tpush.XGPushManager;


public class UserRegisterActivity extends BaseActivity {

    /**
     * 验证码请求
     */
    private int mTimeCount = 0;
    private Handler mAuthCodeTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mTimeCount > 0) {
                mGetAuthCodeBtn.setEnabled(false);
                mGetAuthCodeBtn.setText(StringUtils.getString(mTimeCount, getString(R.string.unit_second)));
                mTimeCount--;
                mAuthCodeTimeHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                mGetAuthCodeBtn.setEnabled(true);
                mGetAuthCodeBtn.setText(R.string.retry);
                mAuthCodeEt.setText("");
                mAuthCodeTimeHandler.removeMessages(0);
            }
        }
    };

    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    private EditText mPhoneEditView;
    private EditText mUserPasswordEt;
    private EditText mAuthCodeEt;
    private TextView mGetAuthCodeBtn;
    private TextView mRegisterTv;
    private CheckBox mPswChoice;
    private ImageView mDeletePswView;
    private Class<?> mTargetActivity;
    private ProgressDialog mProgressDialog;

    private Watcher mEditWatcher;
    private InputMethodManager mInputMethodManager;
    private String mTel;
    private String mPassword;

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
        mEditWatcher = new Watcher();
        isNeedDoubleClick = true;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_userregist;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setVisibility(View.GONE);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getString(R.string.user_register));
        TextView rightView = (TextView) findViewById(R.id.tv_right);
        rightView.setText(R.string.user_login);
        setOnClickListener(rightView);
    }

    @Override
    protected void initContent() {
        mPhoneEditView = (EditText) findViewById(R.id.et_userphone);
        mGetAuthCodeBtn = (TextView) findViewById(R.id.get_auth_code);
        mUserPasswordEt = (EditText) findViewById(R.id.et_userpsw);
        mAuthCodeEt = (EditText) findViewById(R.id.et_authcode);
        mRegisterTv = (TextView) findViewById(R.id.tv_regist);
        mPswChoice = (CheckBox) findViewById(R.id.psw_choice);
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
        mDeletePswView = (ImageView) findViewById(R.id.img_delete);
        setOnClickListener(mGetAuthCodeBtn, mRegisterTv, mDeletePswView);
        mPhoneEditView.addTextChangedListener(mEditWatcher);
        mAuthCodeEt.addTextChangedListener(mEditWatcher);
        mUserPasswordEt.addTextChangedListener(mEditWatcher);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        mSMSBroadcastReceiver = new SMSBroadcastReceiver(mAuthCodeEt);
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(AppConstant.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(1000);
        //注册广播
        registerReceiver(mSMSBroadcastReceiver, intentFilter);
    }

    @Override
    protected void refreshData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                finish();
                jump(mContext, UserLogInActivity.class);
                break;
            case R.id.get_auth_code:
                mAuthCodeEt.setText("");
                sendAuthCode();
                break;
            case R.id.tv_regist:
                register();
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

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消广播接受
        unregisterReceiver(mSMSBroadcastReceiver);
    }

    private void sendAuthCode() {
        // 校验电话号码有效性
        String tel = mPhoneEditView.getText().toString().trim();
        if (StringUtils.checkPhoneNumber(tel)) {
            // 验证码获取等待定时器开启
            if (mTimeCount <= 0) {
                // 提交数据
                NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getAuthCodeParams(mContext, tel), new GetAuthCodeListener(mContext));
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_wait);
            }
        } else {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        }
    }

    private void register() {
        mTel = mPhoneEditView.getText().toString().trim();
        mPassword = mUserPasswordEt.getText().toString().trim();
        String authCode = mAuthCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(mTel)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_empty_number);
        } else if (!StringUtils.checkPhoneNumber(mTel)) {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        } else if (TextUtils.isEmpty(mPassword)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_empty_password);
        } else if (mPassword.length() < IntentConstant.PASSWORD_MIN_LENGTH) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_password_too_short);
        } else if (mPassword.length() > IntentConstant.PASSWORD_MAX_LENGTH) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_password_too_long);
        } else if (authCode.length() != IntentConstant.AUTH_CODE_LENGTH_FOUR && authCode.length() != IntentConstant.AUTH_CODE_LENGTH_SIX) {
            DialogUtils.showShortPromptToast(mContext, R.string.input_identify_code);
        } else {
            NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getRegisterParams(mContext, mTel, authCode, mPassword), new RegisterListener(mContext));
            UmengUtils.onEvent(mContext, EventEnum.UserRegist);
        }
    }

    private void resetTimer() {
        mTimeCount = 0;
        mAuthCodeTimeHandler.removeMessages(0);
    }

    /**
     * 获取验证码
     */
    private class GetAuthCodeListener extends NetUtils.Callback<AuthCodeResponse> {
        public GetAuthCodeListener(Context context) {
            super(context, AuthCodeResponse.class);
        }

        @Override
        public void onNetSuccess(AuthCodeResponse response) {
            if (response.getError()) {
                resetTimer();
            } else {
                //重置定时器
                mTimeCount = IntentConstant.AUTH_CODE_TIME_OUT;
                mAuthCodeTimeHandler.sendEmptyMessage(0);
            }
            DialogUtils.showShortPromptToast(mContext, response.getMessage());
        }

        @Override
        public void onFailure(HttpException e, String s) {
            resetTimer();
        }

        @Override
        public void onConvertFailed(String json) {
            resetTimer();
        }
    }

    /**
     * 注册回调
     */
    private class RegisterListener extends NetUtils.Callback<RegisterResponse> {
        public RegisterListener(Context context) {
            super(context, RegisterResponse.class);
        }

        @Override
        public void onNetSuccess(RegisterResponse response) {
            if (response != null && !response.getError() && response.getData() != null) {
                Toast.makeText(mContext, R.string.registered_success, Toast.LENGTH_SHORT).show();
                //保存信息到本地
                UserInfoUtils.saveUserLoginInfo(mContext, response.getData());
                UserInfoUtils.setSocialLoginFlg(mContext, false);
                //注册成功后，刷新信鸽推送的注册名
                XGPushManager.registerPush(mContext, StringUtils.getString("JQ", UserInfoUtils.getUserId(mContext)), new XGPushCallback(mContext, "XGPush_Regist"));
                //注册之后登录
                mProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.is_login));
                NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getLoginParams(mContext, mTel, mPassword), new LoginStepListener(mContext));
                UmengUtils.onEvent(mContext, EventEnum.UserLogIn);
            } else {
                Toast.makeText(mContext, "Login failed", Toast.LENGTH_SHORT).show();
                UserInfoUtils.setSocialLoginFlg(mContext, false);
            }
        }
    }

    private class LoginStepListener extends NetUtils.Callback<LoginStepResponse> {

        public LoginStepListener(Context context) {
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
                    DialogUtils.showShortPromptToast(mContext, response.getMessage());
                }
                resetTimer();
                return;
            }
            DialogUtils.showShortPromptToast(mContext, R.string.login_success);
            //保存信息到本地
            UserInfoUtils.saveUserLoginInfo(mContext, response.getData());
            UserInfoUtils.saveCacheHeadImagePath(mContext, response.getData().getHeadimg());
            UserInfoUtils.setSocialLoginFlg(mContext, false);
            //登陆成功后，刷新信鸽推送的注册名
            XGPushManager.registerPush(mContext, StringUtils.getString("JQ", UserInfoUtils.getUserId(mContext)), new XGPushCallback(mContext, TAG));
            //跳转回指定页面
            Intent intent;
            if (mTargetActivity != null) {
                intent = new Intent(mContext, mTargetActivity);
            } else {
                intent = new Intent(mContext, MainActivity.class);
            }
            intent.putExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.tv_user);
            jump(intent);
            finish();
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.login_failed);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            resetTimer();
        }

        @Override
        public void onConvertFailed(String json) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            DialogUtils.showShortPromptToast(mContext, R.string.login_failed);
            resetTimer();
        }
    }

    /**
     * 对3个输入框进行监听
     */
    private class Watcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String tel = mPhoneEditView.getText().toString().trim();
            String password = mUserPasswordEt.getText().toString().trim();
            String authCode = mAuthCodeEt.getText().toString().trim();

            if (tel.length() == IntentConstant.PHONE_LENGTH && mTimeCount <= 0) {
                mGetAuthCodeBtn.setBackgroundResource(R.drawable.selector_auth_code_btn);
                mGetAuthCodeBtn.setEnabled(true);
            } else {
                if (mTimeCount <= 0) {
                    mGetAuthCodeBtn.setText(R.string.get_auth_code);
                }
                mGetAuthCodeBtn.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mGetAuthCodeBtn.setEnabled(false);
            }

            if (tel.length() == IntentConstant.PHONE_LENGTH && authCode.length() == IntentConstant.AUTH_CODE_LENGTH_FOUR ||
                    authCode.length() == IntentConstant.AUTH_CODE_LENGTH_SIX && password.length() >= IntentConstant.PASSWORD_MIN_LENGTH &&
                            password.length() <= IntentConstant.PASSWORD_MAX_LENGTH) {
                mRegisterTv.setBackgroundResource(R.drawable.bg_button_corner_4_orange_yellow);
                mRegisterTv.setEnabled(true);
            } else {
                mRegisterTv.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mRegisterTv.setEnabled(false);
            }
            if (TextUtils.isEmpty(password)) {
                mDeletePswView.setVisibility(View.GONE);
            } else {
                mDeletePswView.setVisibility(View.VISIBLE);
            }
        }
    }
}
