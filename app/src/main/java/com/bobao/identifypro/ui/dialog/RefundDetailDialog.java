package com.bobao.identifypro.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;


public class RefundDetailDialog extends BaseCustomerDialog {
    private RelativeLayout mContentRlViewOne;
    private RelativeLayout mContentRlViewTwo;
    private RelativeLayout mContentRlViewThree;
    private TextView mNameOneTvView;
    private TextView mTimeOneTvView;
    private TextView mNameTwoTvView;
    private TextView mTimeTwoTvView;
    private TextView mNameThreeTvView;
    private TextView mTimeThreeTvView;
    private ImageView mDeleteView;
    private String mRefundInfo;

    public RefundDetailDialog(Context context, String extraInfo) {
        super(context, R.style.CustomDialog);
        mRefundInfo = extraInfo;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_refund_detail;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        mContentRlViewOne = (RelativeLayout) findViewById(R.id.rl_content_one);
        mContentRlViewTwo = (RelativeLayout) findViewById(R.id.rl_content_two);
        mContentRlViewThree = (RelativeLayout) findViewById(R.id.rl_content_three);

        mNameOneTvView = (TextView) findViewById(R.id.tv_content_name_one);
        mTimeOneTvView = (TextView) findViewById(R.id.tv_content_time_one);
        mNameTwoTvView = (TextView) findViewById(R.id.tv_content_name_two);
        mTimeTwoTvView = (TextView) findViewById(R.id.tv_content_time_two);
        mNameThreeTvView = (TextView) findViewById(R.id.tv_content_name_three);
        mTimeThreeTvView = (TextView) findViewById(R.id.tv_content_time_three);

        mDeleteView = (ImageView) findViewById(R.id.img_delete);
        setOnClickListener(mDeleteView);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void attachData() {
        if (!TextUtils.isEmpty(mRefundInfo)) {
            String[] strArray = mRefundInfo.split(";");
            if (strArray.length > 0) {
                if (strArray.length == 1 && strArray[0].split(",").length > 0) {
                    mContentRlViewOne.setVisibility(View.VISIBLE);
                    mContentRlViewTwo.setVisibility(View.GONE);
                    mContentRlViewThree.setVisibility(View.GONE);

                    mNameOneTvView.setText(strArray[0].split(",")[0]);
                    mTimeOneTvView.setText(strArray[0].split(",")[1]);
                } else if (strArray.length == 2) {
                    mContentRlViewOne.setVisibility(View.VISIBLE);
                    mContentRlViewTwo.setVisibility(View.VISIBLE);
                    mContentRlViewThree.setVisibility(View.GONE);
                    if (strArray[0].split(",").length > 0) {
                        mNameOneTvView.setText(strArray[0].split(",")[0]);
                        mTimeOneTvView.setText(strArray[0].split(",")[1]);
                    }
                    if (strArray[1].split(",").length > 0) {
                        mNameTwoTvView.setText(strArray[1].split(",")[0]);
                        mTimeTwoTvView.setText(strArray[1].split(",")[1]);
                    }
                } else {
                    mContentRlViewOne.setVisibility(View.VISIBLE);
                    mContentRlViewTwo.setVisibility(View.VISIBLE);
                    mContentRlViewThree.setVisibility(View.VISIBLE);
                    if (strArray[0].split(",").length > 0) {
                        mNameOneTvView.setText(strArray[0].split(",")[0]);
                        mTimeOneTvView.setText(strArray[0].split(",")[1]);
                    }
                    if (strArray[1].split(",").length > 0) {
                        mNameTwoTvView.setText(strArray[1].split(",")[0]);
                        mTimeTwoTvView.setText(strArray[1].split(",")[1]);
                    }
                    if (strArray[2].split(",").length > 0) {
                        mNameThreeTvView.setText(strArray[2].split(",")[0]);
                        mTimeThreeTvView.setText(strArray[2].split(",")[1]);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_delete:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}
