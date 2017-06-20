package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.NormalMessageResponse;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class InviteCodeActivity extends BaseActivity {
    @ViewInject(R.id.tv_back)
    private TextView mTvBack;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.et_invite_code)
    private EditText mInviteCodeEt;
    @ViewInject(R.id.img_delete)
    private ImageView mDeleteView;
    @ViewInject(R.id.tv_finish)
    private TextView mSubmitTv;

    private InputMethodManager mInputMethodManager;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_invite_code;
    }

    @Override
    protected void initTitle() {
        ViewUtils.inject(this, mRootView);
        mTvTitle.setText(R.string.invite_code);
        setOnClickListener(mTvBack);
    }

    @Override
    protected void initContent() {
        mInviteCodeEt.addTextChangedListener(new Watcher());
        setOnClickListener(mDeleteView, mSubmitTv);
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
        String code = mInviteCodeEt.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.img_delete:
                mInviteCodeEt.setText("");
                mInputMethodManager.showSoftInput(mInviteCodeEt, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                break;
            case R.id.tv_finish:
                hideSoftKeyboard();
                if (TextUtils.isEmpty(code)) {
                    DialogUtils.showShortPromptToast(mContext, R.string.invite_not_empty);
                    return;
                }
                submitInviteCode(code);
                break;
            default:
                break;
        }
    }

    private void submitInviteCode(String code) {
        NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getInviteCodeParams(mContext, code), new InviteCodeListener(mContext));
    }

    private class InviteCodeListener extends NetUtils.Callback<NormalMessageResponse> {

        public InviteCodeListener(Context context) {
            super(context, NormalMessageResponse.class);
        }

        @Override
        public void onConvertSuccess(NormalMessageResponse response) {
            if (!response.getError()) {
                onNetSuccess(response);
            } else {
                mInviteCodeEt.setText("");
                mInputMethodManager.showSoftInput(mInviteCodeEt, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                DialogUtils.showShortPromptToast(mContext, response.getMessage());
            }
        }

        @Override
        public void onNetSuccess(NormalMessageResponse response) {
            DialogUtils.showInviteCodeDialog(mContext);
        }
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
            String code = mInviteCodeEt.getText().toString().trim();

            if (!TextUtils.isEmpty(code)) {
                mSubmitTv.setBackgroundResource(R.drawable.selector_auth_code_btn);
                mSubmitTv.setEnabled(true);
            } else {
                mSubmitTv.setBackgroundResource(R.drawable.bg_content_corner_gray9);
                mSubmitTv.setEnabled(false);
            }
            if (TextUtils.isEmpty(code)) {
                mDeleteView.setVisibility(View.GONE);
            } else {
                mDeleteView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 隐藏键盘
     */
    void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
