package com.bobao.identifypro.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.ui.activity.UserIntegrateActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class InviteCodeDialog extends BaseCustomerDialog {
    @ViewInject(R.id.tv_close)
    private TextView mCloseTv;
    @ViewInject(R.id.tv_earn_points)
    private TextView mPointTv;
    @ViewInject(R.id.img_delete)
    private ImageView mDeleteView;

    private Context mContext;

    public InviteCodeDialog(Context context) {
        super(context, R.style.CustomDialog);
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_earn_points:
                ActivityUtils.jump(mContext, UserIntegrateActivity.class);
                dismiss();
                break;
            case R.id.img_delete:
            case R.id.tv_close:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_invite_code;
    }

    @Override
    protected void initTitle() {
        ViewUtils.inject(this, mRootView);
    }

    @Override
    protected void initView() {
        setOnClickListener(mCloseTv, mPointTv, mDeleteView);
        setCancelable(false);
    }

    @Override
    protected void attachData() {

    }
}
