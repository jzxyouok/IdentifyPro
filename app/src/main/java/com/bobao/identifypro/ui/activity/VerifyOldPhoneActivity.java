package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.AuthCodeResponse;
import com.bobao.identifypro.domain.NormalMessageResponse;
import com.bobao.identifypro.receiver.SMSBroadcastReceiver;
import com.bobao.identifypro.ui.dialog.CommonDialog;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.lidroid.xutils.exception.HttpException;

public class VerifyOldPhoneActivity extends BaseActivity {
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
    private EditText mAuthCodeEt;
    private TextView mGetAuthCodeBtn;
    private TextView mSubmitTv;
    private ImageView mDeleteView;

    private CommonDialog mCommonDialog;

    private Watcher mEditWatcher;
    private String mTel;

    private String mPhoneNumber;
    private String mTitle;

    private boolean isSubmit;

    private InputMethodManager mInputMethodManager;

    private String mTargetActivityName;

    private View.OnClickListener mCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCommonDialog.dismiss();
        }
    };
    private View.OnClickListener mSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void getIntentData() {
        mPhoneNumber = getIntent().getStringExtra(IntentConstant.PHONE_NUMBER);
        mTitle = getIntent().getStringExtra(IntentConstant.ACTIVITY_TITLE);

        mTargetActivityName = getIntent().getStringExtra(IntentConstant.TARGET_ACTIVITY);
    }

    @Override
    protected void initData() {
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditWatcher = new Watcher();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_edit_phone;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(mTitle);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mPhoneEditView = (EditText) findViewById(R.id.et_phone);
        mPhoneEditView.setHint(R.string.input_check_phone_number);
        mAuthCodeEt = (EditText) findViewById(R.id.et_authcode);
        mGetAuthCodeBtn = (TextView) findViewById(R.id.get_auth_code);
        mSubmitTv = (TextView) findViewById(R.id.tv_submit);
        mSubmitTv.setText(R.string.next_step);
        mDeleteView = (ImageView) findViewById(R.id.img_delete);
        setOnClickListener(mGetAuthCodeBtn, mSubmitTv, mDeleteView);
        mPhoneEditView.addTextChangedListener(mEditWatcher);
        mAuthCodeEt.addTextChangedListener(mEditWatcher);

        if (!TextUtils.isEmpty(mPhoneNumber)) {
            mPhoneEditView.setText(mPhoneNumber);
        } else {
            mPhoneEditView.setText(UserInfoUtils.getPhone(mContext));
        }

        CharSequence mPhoneEtText = mPhoneEditView.getText();
        if (mPhoneEtText != null) {
            Spannable spanText = (Spannable) mPhoneEtText;
            Selection.setSelection(spanText, mPhoneEtText.length());
        }
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
            case R.id.tv_back:
                if (isSubmit) {
                    finish();
                } else {
                    showPickDialog();
                }
                break;
            case R.id.get_auth_code:
                mAuthCodeEt.setText("");
                sendAuthCode();
                break;
            case R.id.tv_submit:
                checkPhoneNumber();
                break;
            case R.id.img_delete:
                mPhoneEditView.setText("");
                mInputMethodManager.showSoftInput(mPhoneEditView, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    /**
     * 选择提示对话框
     */
    private void showPickDialog() {
        mCommonDialog = DialogUtils.showCommonDialog(mContext, getString(R.string.prompt), getString(R.string.cancel_modify_auth_code),
                getString(R.string.continue_modify), getString(R.string.confirm_cancel), mSubmitListener, mCancelListener);
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
                NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getCheckPhoneAuthCodeParams(mContext, tel), new GetAuthCodeListener(mContext));
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_wait);
            }
        } else {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        }
    }

    private void checkPhoneNumber() {
        mTel = mPhoneEditView.getText().toString().trim();
        String authCode = mAuthCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(mTel)) {
            DialogUtils.showShortPromptToast(mContext, R.string.auth_code_tip_empty_number);
        } else if (!StringUtils.checkPhoneNumber(mTel)) {
            DialogUtils.showShortPromptToast(mContext, R.string.tip_wrong_number);
        } else if (authCode.length() != IntentConstant.AUTH_CODE_LENGTH_FOUR && authCode.length() != IntentConstant.AUTH_CODE_LENGTH_SIX) {
            DialogUtils.showShortPromptToast(mContext, R.string.input_identify_code);
        } else {
            NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getCheckPhoneSubmitParams(mContext, mTel, authCode), new CheckPhoneListener(mContext));
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
     * 认证手机号回调
     */
    private class CheckPhoneListener extends NetUtils.Callback<NormalMessageResponse> {
        public CheckPhoneListener(Context context) {
            super(context, NormalMessageResponse.class);
        }

        @Override
        public void onNetSuccess(NormalMessageResponse response) {
            if (response != null && !response.getError() && response.getData() != null) {
                isSubmit = true;
                UserInfoUtils.setPhone(mContext, mTel);
                DialogUtils.showShortPromptToast(mContext, R.string.check_phone_success);
                if (!TextUtils.isEmpty(mTargetActivityName)) {
                    switch (mTargetActivityName) {
                        case IntentConstant.ACTIVITY_FROM_EDIT_PHONE:
                            Intent intent = new Intent();
                            intent.putExtra(IntentConstant.CHECK_PHONE_FLAG, true);
                            setResult(RESULT_OK, intent);
                            break;
                        case IntentConstant.ACTIVITY_FROM_EDIT_PASSWORD:
                            jump(mContext, EditPassWordActivity.class);
                            break;
                        default:
                            break;
                    }
                }
                finish();
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.check_phone_failed);
                resetTimer();
            }
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
                    authCode.length() == IntentConstant.AUTH_CODE_LENGTH_SIX) {
                mSubmitTv.setBackgroundResource(R.drawable.bg_button_corner_4_orange_yellow);
                mSubmitTv.setEnabled(true);
            } else {
                mSubmitTv.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mSubmitTv.setEnabled(false);
            }

            if (TextUtils.isEmpty(tel)) {
                mDeleteView.setVisibility(View.GONE);
            } else {
                mDeleteView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSubmit) {
                finish();
            } else {
                showPickDialog();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
