package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.EditNickNameResponse;
import com.bobao.identifypro.ui.dialog.CommonDialog;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.lidroid.xutils.exception.HttpException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class EditNickNameActivity extends BaseActivity {
    private EditText mNickNameEt;
    private ImageView mDeleteImg;
    private View mCommitBtn;

    private String mNickName;
    private boolean mIsSubmit;

    private CommonDialog mCommonDialog;

    private InputMethodManager mInputMethodManager;

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
        Intent intent = getIntent();
        mNickName = intent.getStringExtra(UserPrivateInfoActivity.USER_NICKNAME);
    }

    @Override
    protected void initData() {
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_edit_nickname;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(R.string.modify_nickname);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mNickNameEt = (EditText) findViewById(R.id.et_nickname);
        mDeleteImg = (ImageView) findViewById(R.id.img_delete_nickname);
        mCommitBtn = findViewById(R.id.btn_edit_nickname_commit);

        mNickNameEt.addTextChangedListener(new Watcher());
        setOnClickListener(mCommitBtn, mDeleteImg);

        if (!TextUtils.isEmpty(mNickName)) {
            mNickNameEt.setText(mNickName);
            CharSequence mNickNameEtText = mNickNameEt.getText();
            if (mNickNameEtText != null) {
                Spannable spanText = (Spannable) mNickNameEtText;
                Selection.setSelection(spanText, mNickNameEtText.length());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                if (mIsSubmit) {
                    finish();
                } else {
                    showPickDialog();
                }
                break;
            case R.id.btn_edit_nickname_commit:
                if (!UserInfoUtils.checkUserLogin(EditNickNameActivity.this)) {
                    Toast.makeText(this, R.string.not_login, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, UserLogInActivity.class);
                    startActivity(intent);
                    return;
                }
                String nickNameStr = mNickNameEt.getText().toString().trim();
                if (TextUtils.isEmpty(nickNameStr)) {
                    Toast.makeText(this, R.string.nickname_can_not_be_empty, Toast.LENGTH_SHORT).show();
                    mNickNameEt.setText("");
                    return;
                }
                if (nickNameStr.length() > IntentConstant.NICKNAME_MAX_LENGTH) {
                    Toast.makeText(this, R.string.nickname_length_to_long, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    // 仅需对昵称做URL编码
                    nickNameStr = URLEncoder.encode(nickNameStr, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                commitEditNicknameRequest(nickNameStr);
                break;
            case R.id.img_delete_nickname:
                mNickNameEt.setText("");
                mInputMethodManager.showSoftInput(mNickNameEt, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                break;
        }
    }

    private void commitEditNicknameRequest(String nickName) {
        NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getCommitEditNicknameParams(mContext, nickName), new EditNickNameListener(mContext));
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

    private class EditNickNameListener extends NetUtils.Callback<EditNickNameResponse> {

        public EditNickNameListener(Context context) {
            super(context, EditNickNameResponse.class);
        }

        @Override
        public void onConvertFailed(String json) {
            DialogUtils.showShortPromptToast(mContext, R.string.modify_nickname_fail);
        }

        @Override
        public void onNetSuccess(EditNickNameResponse response) {
            if (response == null) {
                DialogUtils.showShortPromptToast(mContext, R.string.modify_nickname_fail);
                return;
            }
            if (response.getError()) {
                DialogUtils.showShortPromptToast(mContext, R.string.nickname_length_to_long);
            } else {
                UserInfoUtils.saveNickName(mContext, mNickNameEt.getText().toString().trim());
                mIsSubmit = true;
                DialogUtils.showShortPromptToast(mContext, R.string.modify_nickname_success);
                finish();
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.modify_nickname_fail);
        }
    }

    /**
     * 选择提示对话框
     */
    private void showPickDialog() {
        mCommonDialog = DialogUtils.showCommonDialog(mContext, getString(R.string.prompt), getString(R.string.cancel_modify_nickname),
                getString(R.string.continue_modify), getString(R.string.confirm_cancel), mSubmitListener, mCancelListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsSubmit) {
                finish();
            } else {
                showPickDialog();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class Watcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String nickname = mNickNameEt.getText().toString().trim();

            if (nickname.length() >= IntentConstant.NICKNAME_MIN_LENGTH && nickname.length() <= IntentConstant.NICKNAME_MAX_LENGTH) {
                mCommitBtn.setBackgroundResource(R.drawable.bg_button_corner_4_orange_yellow);
                mCommitBtn.setEnabled(true);
            } else {
                mCommitBtn.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mCommitBtn.setEnabled(false);
            }
            if (TextUtils.isEmpty(nickname)) {
                mDeleteImg.setVisibility(View.GONE);
            } else {
                mDeleteImg.setVisibility(View.VISIBLE);
            }
        }
    }
}
