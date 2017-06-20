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

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.AuthCodeResponse;
import com.bobao.identifypro.receiver.SMSBroadcastReceiver;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.lidroid.xutils.exception.HttpException;

/**
 * Created by you on 2015/6/23.
 */
public class ForgetPswActivity extends BaseActivity {
    private EditText mMobileEt;
    private EditText mAuthCodeEt;
    private TextView mGetAuthCodeTv;

    private EditText mNewPswdEt;
    private TextView mChangePswd;
    private ImageView mDeletePswView;
    private CheckBox mPswChoice;

    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    private Watcher mEditWatcher;
    private InputMethodManager mInputMethodManager;

    private int mTimeCount = 0;
    private Handler mAuthCodeTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mTimeCount > 0) {
                mGetAuthCodeTv.setText(StringUtils.getString(mTimeCount, "s"));
                mTimeCount--;
                mAuthCodeTimeHandler.sendEmptyMessageDelayed(0, 1000);
            } else {
                mGetAuthCodeTv.setText(R.string.retry);
                mAuthCodeEt.setText("");
                mAuthCodeTimeHandler.removeMessages(0);
            }
        }
    };

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditWatcher = new Watcher();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_forget_pswd;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(R.string.forget_psw);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mMobileEt = (EditText) findViewById(R.id.et_userphone);
        mAuthCodeEt = (EditText) findViewById(R.id.et_authcode);
        mGetAuthCodeTv = (TextView) findViewById(R.id.get_auth_code);
        mPswChoice = (CheckBox) findViewById(R.id.psw_choice);
        mDeletePswView = (ImageView) findViewById(R.id.img_delete);
        mNewPswdEt = (EditText) findViewById(R.id.et_userpsw);
        mChangePswd = (TextView) findViewById(R.id.tv_finish);
        setOnClickListener(mGetAuthCodeTv, mDeletePswView, mChangePswd);

        mSMSBroadcastReceiver = new SMSBroadcastReceiver(mAuthCodeEt);

        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(AppConstant.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(1001);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);
        mPswChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mNewPswdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mNewPswdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                CharSequence mPhoneEtText = mNewPswdEt.getText();
                if (mPhoneEtText != null) {
                    Spannable spanText = (Spannable) mPhoneEtText;
                    Selection.setSelection(spanText, mPhoneEtText.length());
                }
            }
        });
        mMobileEt.addTextChangedListener(mEditWatcher);
        mAuthCodeEt.addTextChangedListener(mEditWatcher);
        mNewPswdEt.addTextChangedListener(mEditWatcher);
    }

    @Override
    public void onClick(View view) {
        String tel = mMobileEt.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.get_auth_code:
                mAuthCodeEt.setText("");
                sendAuthCode(tel);
                break;
            case R.id.tv_finish:
                changePwd(tel);
                break;
            case R.id.img_delete:
                mNewPswdEt.setText("");
                mInputMethodManager.showSoftInput(mNewPswdEt, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                break;
            default:
                break;
        }
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
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mSMSBroadcastReceiver);
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

    private void sendAuthCode(String tel) {
        // 校验电话号码有效性
        if (StringUtils.checkPhoneNumber(tel)) {
            // 验证码获取等待定时器开启
            if (mTimeCount <= 0) {
                // 提交数据
                NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getAuthCodeRequestParams(mContext, tel), new AuthCodeListener(mContext));
                //重置定时器
                mTimeCount = IntentConstant.AUTH_CODE_TIME_OUT;
                mAuthCodeTimeHandler.sendEmptyMessage(0);
                // 设置不可点击
                mGetAuthCodeTv.setEnabled(false);
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_wait);
            }
        } else {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        }
    }

    private void changePwd(String tel) {
        String newPswd = mNewPswdEt.getText().toString().trim();
        String authCode = mAuthCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(newPswd)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_empty_password);
        } else if (newPswd.length() < IntentConstant.PASSWORD_MIN_LENGTH) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_password_too_short);
        } else if (newPswd.length() > IntentConstant.PASSWORD_MAX_LENGTH) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_password_too_long);
        } else if ((authCode.length() != IntentConstant.AUTH_CODE_LENGTH_FOUR) && (authCode.length() != IntentConstant.AUTH_CODE_LENGTH_SIX)) {
            DialogUtils.showShortPromptToast(mContext, R.string.input_identify_code);
        } else {
            NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getChangePasswdRequestParams(mContext, tel, authCode, newPswd), new ChangePasswdListener(mContext));
        }
    }

    private class ChangePasswdListener extends NetUtils.Callback<AuthCodeResponse> {

        public ChangePasswdListener(Context context) {
            super(context, AuthCodeResponse.class);
        }

        @Override
        public void onNetSuccess(AuthCodeResponse response) {
            if (response.getError()) {
                DialogUtils.showShortPromptToast(mContext, R.string.forget_psw_modify_failed);
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.forget_psw_modify_success);
                Intent intent = new Intent(mContext, UserLogInActivity.class);
                jump(intent);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.forget_psw_modify_failed);
        }

        @Override
        public void onConvertFailed(String json) {
            DialogUtils.showShortPromptToast(mContext, R.string.forget_psw_modify_failed);
        }
    }

    private class AuthCodeListener extends NetUtils.Callback<AuthCodeResponse> {

        public AuthCodeListener(Context context) {
            super(context, AuthCodeResponse.class);
        }

        @Override
        public void onConvertFailed(String json) {
            resetTimer();
        }

        @Override
        public void onNetSuccess(AuthCodeResponse response) {
            resetTimer();
            DialogUtils.showShortPromptToast(mContext, response.getMessage());
        }

        @Override
        public void onFailure(HttpException e, String s) {
            resetTimer();
        }
    }

    private void resetTimer() {
        if (mTimeCount > IntentConstant.AUTH_CODE_TIME_OUT) {
            mTimeCount = IntentConstant.AUTH_CODE_TIME_OUT;
            mAuthCodeTimeHandler.removeMessages(0);
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
            String tel = mMobileEt.getText().toString().trim();
            String password = mNewPswdEt.getText().toString().trim();
            String authCode = mAuthCodeEt.getText().toString().trim();

            if (tel.length() == IntentConstant.PHONE_LENGTH && mTimeCount <= 0) {
                mGetAuthCodeTv.setBackgroundResource(R.drawable.selector_auth_code_btn);
                mGetAuthCodeTv.setEnabled(true);
            } else {
                if (mTimeCount <= 0) {
                    mGetAuthCodeTv.setText(R.string.get_auth_code);
                }
                mGetAuthCodeTv.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mGetAuthCodeTv.setEnabled(false);
            }

            if (tel.length() == IntentConstant.PHONE_LENGTH &&
                    (authCode.length() == IntentConstant.AUTH_CODE_LENGTH_FOUR || authCode.length() == IntentConstant.AUTH_CODE_LENGTH_SIX) &&
                    password.length() >= IntentConstant.PASSWORD_MIN_LENGTH && password.length() <= IntentConstant.PASSWORD_MAX_LENGTH) {
                mChangePswd.setBackgroundResource(R.drawable.bg_button_corner_4_orange_yellow);
                mChangePswd.setEnabled(true);
            } else {
                mChangePswd.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mChangePswd.setEnabled(false);
            }
            if (TextUtils.isEmpty(password)) {
                mDeletePswView.setVisibility(View.GONE);
            } else {
                mDeletePswView.setVisibility(View.VISIBLE);
            }
        }
    }
}
