package com.bobao.identifypro.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bobao.identifypro.R;


/**
 * Created by you on 2015/6/1.
 */
public class EditInfoDialog extends BaseCustomerDialog{

    private EditText mEditView;
    private OnConfirmListener mConfirmListener;
    private int mTitleRes;
    private int mHintRes;

    public interface OnConfirmListener{
        void onConfirm(String text);
    }

    public EditInfoDialog(Context context, OnConfirmListener confirmListener, int titleRes, int hintRes) {
        super(context, R.style.CustomDialog);
        mConfirmListener = confirmListener;
        mTitleRes = titleRes;
        mHintRes = hintRes;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_edit_info;
    }

    @Override
    protected void initTitle() {
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(mTitleRes);
    }

    @Override
    protected void initView() {
        View cancelButton = findViewById(R.id.tv_cancel);
        View confirmButton = findViewById(R.id.tv_ok);
        mEditView = (EditText) findViewById(R.id.et_dialog_info);
        setOnClickListener(cancelButton, confirmButton);
    }

    @Override
    protected void attachData() {
        if (mHintRes > 0) {
            mEditView.setHint(mHintRes);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_ok:
                if(mConfirmListener != null){
                    mConfirmListener.onConfirm(String.valueOf(mEditView.getText()));
                }
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}
