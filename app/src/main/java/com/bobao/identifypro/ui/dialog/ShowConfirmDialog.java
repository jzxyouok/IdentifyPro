package com.bobao.identifypro.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;

public class ShowConfirmDialog extends BaseCustomerDialog {
    private TextView mDesView;
    private TextView mOkView;

    private Context mContext;
    private String mTitle;
    private String mContent;

    public ShowConfirmDialog(Context context, String title, String content) {
        super(context, R.style.CustomDialog);
        mContext = context;
        mTitle = title;
        mContent = content;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_confirm;
    }

    @Override
    protected void initTitle() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(mTitle);
    }

    @Override
    protected void initView() {
        mDesView = (TextView) findViewById(R.id.tv_content);
        mOkView = (TextView) findViewById(R.id.tv_ok);
        setOnClickListener(mOkView);
    }

    @Override
    protected void attachData() {
        mDesView.setText(mContent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                dismiss();
                ((Activity) mContext).finish();
                break;
            default:
                break;
        }
    }
}
