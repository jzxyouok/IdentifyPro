package com.bobao.identifypro.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;

public class ExitDialog extends BaseCustomerDialog {

    private String mTitleStr;
    private String mCancelStr;
    private String mSubmitStr;
    private View.OnClickListener mSubmitListener;
    private View.OnClickListener mCancelListener;

    public ExitDialog(Context context, String strTitle, String strNeg, String strPos,
                      View.OnClickListener mSubmitListener, View.OnClickListener mCancelListener) {
        super(context, R.style.CustomDialog);
        setCanceledOnTouchOutside(false);
        this.mTitleStr = strTitle;
        this.mCancelStr = strNeg;
        this.mSubmitStr = strPos;
        this.mSubmitListener = mSubmitListener;
        this.mCancelListener = mCancelListener;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_exit;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        TextView mTitle = (TextView) findViewById(R.id.dialog_tv_title);
        TextView mCancel = (TextView) findViewById(R.id.tv_cancel);
        TextView mSubmit = (TextView) findViewById(R.id.tv_ok);
        mTitle.setText(mTitleStr);
        mCancel.setText(mCancelStr);
        mSubmit.setText(mSubmitStr);
        setCanceledOnTouchOutside(true);
        setOnClickListener(mCancel, mSubmit);
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                mSubmitListener.onClick(v);
                break;
            case R.id.tv_cancel:
                mCancelListener.onClick(v);
                break;
            default:
                dismiss();
                break;
        }
    }
}
