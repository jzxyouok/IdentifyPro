package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.EditPassWdResponse;
import com.bobao.identifypro.ui.dialog.CommonDialog;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.lidroid.xutils.exception.HttpException;

/**
 * Created by you on 2015/6/17.
 */
public class EditPassWordActivity extends BaseActivity {
    private EditText mOldPaswdEt;
    private EditText mNewPaswdEt1;
    private EditText mNewPaswdEt2;
    private View mCommitBtn;

    private Watcher mEditWatcher;
    private CommonDialog mCommonDialog;
    private boolean isSubmit;

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

    private View.OnClickListener mLoginCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCommonDialog.dismiss();
        }
    };
    private View.OnClickListener mLoginSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            Intent intent = new Intent(mContext, UserLogInActivity.class);
            jump(intent);
        }
    };

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {
        mEditWatcher = new Watcher();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_edit_password;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(getString(R.string.modify_password));
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mOldPaswdEt = (EditText) findViewById(R.id.et_old_password);
        mNewPaswdEt1 = (EditText) findViewById(R.id.et_new_password1);
        mNewPaswdEt2 = (EditText) findViewById(R.id.et_new_password2);
        mCommitBtn = findViewById(R.id.tv_submit);

        setOnClickListener(mCommitBtn);
        mOldPaswdEt.addTextChangedListener(mEditWatcher);
        mNewPaswdEt1.addTextChangedListener(mEditWatcher);
        mNewPaswdEt2.addTextChangedListener(mEditWatcher);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                if (isSubmit) {
                    finish();
                } else {
                    showPickDialog(R.string.prompt, R.string.cancel_modify_password, mSubmitListener, mCancelListener);
                }
                break;
            case R.id.tv_submit:
                if (!UserInfoUtils.checkUserLogin(mContext)) {
                    showPickDialog(R.string.not_login, R.string.go_to_login_or_register, mLoginSubmitListener, mLoginCancelListener);
                    return;
                }
                String oldPswd = mOldPaswdEt.getText().toString().trim();
                String newPswd1 = mNewPaswdEt1.getText().toString().trim();
                String newPswd2 = mNewPaswdEt2.getText().toString().trim();
                if (TextUtils.isEmpty(oldPswd)) {
                    DialogUtils.showShortPromptToast(mContext, R.string.fill_in_original_password);
                    return;
                }
                if (newPswd1.length() < IntentConstant.PASSWORD_MIN_LENGTH || newPswd1.length() > IntentConstant.PASSWORD_MAX_LENGTH) {
                    DialogUtils.showShortPromptToast(mContext, R.string.new_password_between);
                    return;
                }
                if (TextUtils.isEmpty(newPswd2) || !newPswd2.equals(newPswd1)) {
                    DialogUtils.showShortPromptToast(mContext, R.string.two_input_password_different);
                    return;
                }
                //TODO 提交密码修改信息
                commitEditPasswordRequest(oldPswd, newPswd1);
                break;
        }
    }

    private void commitEditPasswordRequest(String oldPwd, String newPwd) {
        NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getEditPasswordParams(mContext, oldPwd, newPwd), new EditPasswordListener(mContext));
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
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }

    private class EditPasswordListener extends NetUtils.Callback<EditPassWdResponse> {

        public EditPasswordListener(Context context) {
            super(context, EditPassWdResponse.class);
        }

        @Override
        public void onConvertFailed(String json) {
            DialogUtils.showShortPromptToast(mContext, R.string.modify_password_fail);
        }

        @Override
        public void onNetSuccess(EditPassWdResponse response) {
            if (response == null || response.getError()) {
                DialogUtils.showShortPromptToast(mContext, R.string.modify_password_fail);
                return;
            }
            isSubmit = true;
            DialogUtils.showShortPromptToast(mContext, R.string.modify_password_success);
            // 重新显示登陆页面
            finish();
            jump(mContext, UserLogInActivity.class);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.modify_password_fail);
        }
    }

    /**
     * 登录提示对话框
     */
    private void showPickDialog(int title, int content, View.OnClickListener submitListener, View.OnClickListener cancelListener) {
        mCommonDialog = DialogUtils
                .showCommonDialog(mContext, getString(title), getString(content), getString(R.string.negative), getString(R.string.positive), submitListener, cancelListener);
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
            String oldPswd = mOldPaswdEt.getText().toString().trim();
            String newPswd1 = mNewPaswdEt1.getText().toString().trim();
            String newPswd2 = mNewPaswdEt2.getText().toString().trim();

            boolean pswd = oldPswd.length() >= IntentConstant.PASSWORD_MIN_LENGTH && oldPswd.length() <= IntentConstant.PASSWORD_MAX_LENGTH;
            boolean newpswd1 = newPswd1.length() >= IntentConstant.PASSWORD_MIN_LENGTH && newPswd1.length() <= IntentConstant.PASSWORD_MAX_LENGTH;
            boolean newpswd2 = newPswd2.length() >= IntentConstant.PASSWORD_MIN_LENGTH && newPswd2.length() <= IntentConstant.PASSWORD_MAX_LENGTH;

            if (pswd && newpswd1 && newpswd2) {
                mCommitBtn.setBackgroundResource(R.drawable.bg_button_corner_4_orange_yellow);
                mCommitBtn.setEnabled(true);
            } else {
                mCommitBtn.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mCommitBtn.setEnabled(false);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSubmit) {
                finish();
            } else {
                showPickDialog(R.string.prompt, R.string.cancel_modify_password, mSubmitListener, mCancelListener);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
