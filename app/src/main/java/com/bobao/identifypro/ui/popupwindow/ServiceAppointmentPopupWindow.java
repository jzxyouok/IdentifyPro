package com.bobao.identifypro.ui.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.domain.ExpertServiceInfoResponse;
import com.bobao.identifypro.domain.ServiceAppointmentResponse;
import com.bobao.identifypro.ui.widget.wheelview.LoopView;
import com.bobao.identifypro.ui.widget.wheelview.OnItemSelectedListener;
import com.bobao.identifypro.utils.SizeUtils;
import com.bobao.identifypro.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ServiceAppointmentPopupWindow extends PopupWindow {
    private View mContentView;
    private Context mContext;
    private RelativeLayout mWheelViewRl;
    private TextView mFinishTv;
    private View mMaskView;
    private RelativeLayout.LayoutParams mLayoutParams;
    private LoopView mLoopView;

    private List<String> mData;
    private int mStatus;
    private View.OnClickListener mListener;
    private int mPosition;


    public ServiceAppointmentPopupWindow(Activity context, List<String> strArray, View.OnClickListener listener, int status, int position) {
        this.mContext = context;
        mData = strArray;
        mListener = listener;
        mStatus = status;
        mPosition = position;
        initView();
    }

    public ServiceAppointmentPopupWindow(Activity context, List<ServiceAppointmentResponse.DataEntity.ListEntity> identifyList, View.OnClickListener listener, int position) {
        this.mContext = context;
        mListener = listener;
        mStatus = IntentConstant.EXPERTS_LIST;
        mPosition = position;
        if (identifyList != null && identifyList.size() > 0) {
            mData = new ArrayList<>();
            for (ServiceAppointmentResponse.DataEntity.ListEntity listEntity : identifyList) {
                mData.add(StringUtils
                        .getString(listEntity.getOrg(), "  ", listEntity.getName(), "  ", listEntity.getPrice(), mContext.getString(R.string.unit_of_money), listEntity.getTime()));
            }
        }
        initView();
    }

    public ServiceAppointmentPopupWindow(Activity context, ExpertServiceInfoResponse expertServiceInfoResponse, View.OnClickListener listener, int position) {
        this.mContext = context;
        mListener = listener;
        mStatus = IntentConstant.EXPERT_SERVICE_TYPE;
        mPosition = position;
        if (expertServiceInfoResponse.getData() != null && expertServiceInfoResponse.getData().size() > 0) {
            mData = new ArrayList<>();
            for (ExpertServiceInfoResponse.DataEntity dataEntity : expertServiceInfoResponse.getData()) {
                mData.add(StringUtils.getString(dataEntity.getName(), "    ", dataEntity.getPrice(), mContext.getString(R.string.unit_of_money), dataEntity.getUnit()));
            }
        }
        initView();
    }

    private void initView() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_service_appointment, null);
        // 设置SelectPicPopupWindow的View
        setContentView(mContentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
//        setTouchable(true);
//        setFocusable(true);
//        setBackgroundDrawable(new BitmapDrawable());
//        setOutsideTouchable(true);
        // 刷新状态
        update();
        setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(R.style.AnimationPreview);

        mLoopView = new LoopView(mContext);
        mLoopView.setPadding(0, (int) SizeUtils.dp2Px(mContext.getResources(), 10.0f), 0, (int) SizeUtils.dp2Px(mContext.getResources(), 10.0f));
        mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        mWheelViewRl = (RelativeLayout) mContentView.findViewById(R.id.rl_wheel_view);
        mFinishTv = (TextView) mContentView.findViewById(R.id.tv_finish);
        mFinishTv.setTag(R.id.service_appointment_status, mStatus);
        mFinishTv.setTag(mPosition);
        mFinishTv.setOnClickListener(mListener);
        //设置原始数据
        mLoopView.setItems(mData);
        //设置初始位置
        mLoopView.setInitPosition(mPosition);
        //设置字体大小
        mLoopView.setTextSize(14);
        mWheelViewRl.addView(mLoopView, mLayoutParams);

        //设置是否循环播放
        mLoopView.setNotLoop();
        //滚动监听
        mLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mFinishTv.setTag(R.id.service_appointment_status, mStatus);
                mFinishTv.setTag(index);
            }
        });
        mMaskView = mContentView.findViewById(R.id.mask_view);
        mMaskView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dismiss();
                return true;
            }
        });
    }

    /**
     * 显示popupWindow
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
