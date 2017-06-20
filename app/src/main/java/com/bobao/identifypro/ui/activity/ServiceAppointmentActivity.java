package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.EditServiceAppointmentResponse;
import com.bobao.identifypro.domain.GetOrderIdResponse;
import com.bobao.identifypro.domain.ServiceAppointmentResponse;
import com.bobao.identifypro.ui.dialog.CommonDialog;
import com.bobao.identifypro.ui.popupwindow.ServiceAppointmentPopupWindow;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceAppointmentActivity extends BaseActivity {
    private TextView mReservationExpertsTvServiceTypeTv;
    private RelativeLayout mReservationExpertRl;
    private TextView mReservationExpertsTv;

    private RelativeLayout mExpertsListLL;
    private TextView mExpertsListTv;
    private RelativeLayout mServiceTypeRl;
    private TextView mServiceTypeTv;

    private RelativeLayout mAppraisalCostRL;
    private TextView mAppraisalCostTv;

    private TextView mCollectionTypeTv;

    private RelativeLayout mCollectionNumRL;
    private TextView mCollectionNumTv;

    private EditText mSpecificationIdentificationEdt;
    private TextView mSubmitReservationTv;

    private TextView mTitleView;

    private LinearLayout mMoneyDetailLL;
    private TextView mMoneyTv;
    private TextView mModifyTv;
    private TextView mPayTv;

    private RelativeLayout mSubmitRl;

    private ServiceAppointmentPopupWindow mServiceAppointmentPopupWindow;
    private int mReservationExpertsPosition;
    private int mExpertsListPosition;
    private int mCollectionNumPosition;
    private int mServiceInfoPosition;


    private double mLatitude;
    private double mLontitude;

    private List<String> mReservationExpertsList;
    private ServiceAppointmentResponse.DataEntity mIdentifyEntity;
    private List<String> mCollectionNumList;

    private String mActivityTarget;

    private String mExpertId;
    private String mExpertName = "";
    private String mIdentifyType = "";
    private String mIdentifyTypeName = "";
    private String mServiceType = "";
    private String mServiceTypeName = "";
    private String mAppraisalCost = "";
    private int meet = 0;

    private ArrayList<String> mSerivceTypeIdList;
    private ArrayList<String> mSerivceTypeNameList;
    private ArrayList<String> mServiceTypePriceList;
    private ArrayList<String> mServiceTypeInfoList;

    private String mAppointmentIdentifyId;
    private String mAppointmentIdentifyType;

    private CommonDialog mCommonDialog;

    private String[] reservationExpertsArray;

    private View.OnClickListener mCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCommonDialog != null) {
                mCommonDialog.dismiss();
            }
        }
    };
    private View.OnClickListener mFinishSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UmengUtils.onEvent(mContext, EventEnum.Service_Appointment_Cancel_Onclick);
            finish();
        }
    };

    private View.OnClickListener mLoginSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContext != null) {
                Intent intent = new Intent(mContext, UserLogInActivity.class);
                jump(intent);
                if (mCommonDialog != null) {
                    mCommonDialog.dismiss();
                }
            }
        }
    };

    private View.OnClickListener mCheckPhoneSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContext != null) {
                //跳转校验手机号页面
                Intent intent = new Intent();
                intent.setClass(mContext, VerifyOldPhoneActivity.class);
                intent.putExtra(IntentConstant.PHONE_NUMBER, "");
                intent.putExtra(IntentConstant.ACTIVITY_TITLE, getString(R.string.phone_certification));
                intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_EDIT_PHONE);
                jump(intent, IntentConstant.RequestCodeCheckPhoneNumber);
                if (mCommonDialog != null) {
                    mCommonDialog.dismiss();
                }
            }
        }
    };

    @Override
    protected void getIntentData() {
        mActivityTarget = getIntent().getStringExtra(IntentConstant.TARGET_ACTIVITY);
        mExpertId = getIntent().getStringExtra(IntentConstant.EXPERT_ID);
        mExpertName = getIntent().getStringExtra(IntentConstant.EXPERT_NAME);
        mServiceType = getIntent().getStringExtra(IntentConstant.SERVICE_TYPE);
        mServiceTypeName = getIntent().getStringExtra(IntentConstant.SERVICE_TYPE_NAME);
        mIdentifyType = getIntent().getStringExtra(IntentConstant.IDENTIFY_TYPE);
        mIdentifyTypeName = getIntent().getStringExtra(IntentConstant.IDENTIFY_TYPE_NAME);
        mAppraisalCost = getIntent().getStringExtra(IntentConstant.APPRAISAL_COST);

        mSerivceTypeIdList = getIntent().getStringArrayListExtra(IntentConstant.SERVICE_TYPE_ID_LIST);
        mSerivceTypeNameList = getIntent().getStringArrayListExtra(IntentConstant.SERVICE_TYPE_NAME_LIST);
        mServiceTypePriceList = getIntent().getStringArrayListExtra(IntentConstant.SERVICE_TYPE_PRICE_LIST);
        mServiceTypeInfoList = getIntent().getStringArrayListExtra(IntentConstant.SERVICE_TYPE_INFO_LIST);

        if (!TextUtils.isEmpty(mServiceType) && Integer.valueOf(mServiceType) > 0) {
            mServiceInfoPosition = Integer.valueOf(mServiceType) - 1;
        }

        mAppointmentIdentifyId = getIntent().getStringExtra(IntentConstant.APPOINTMENT_IDENTIFY_ID);
        mAppointmentIdentifyType = getIntent().getStringExtra(IntentConstant.APPOINTMENT_IDENTIFY_TYPE);
    }

    @Override
    protected void initData() {
        mLatitude = UserInfoUtils.getUserLatitude(mContext);
        mLontitude = UserInfoUtils.getUserLongitude(mContext);
        reservationExpertsArray = getResources().getStringArray(R.array.reservation_experts);
        if (reservationExpertsArray != null && reservationExpertsArray.length > 0) {
            mReservationExpertsList = Arrays.asList(reservationExpertsArray);
        }
        String[] collectionNumArray = getResources().getStringArray(R.array.collection_num);
        if (collectionNumArray != null && collectionNumArray.length > 0) {
            mCollectionNumList = Arrays.asList(collectionNumArray);
        }
        mReservationExpertsPosition = 0;
        mExpertsListPosition = 0;
        mCollectionNumPosition = 0;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_service_appointment;
    }

    @Override
    protected void initTitle() {
        if (!TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY, mActivityTarget)) {//查看我的预约专家详情页
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        TextView backView = (TextView) findViewById(R.id.tv_back);
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mTitleView.setText(mServiceTypeName);
        if (mServiceType != null && AppConstant.SERVICE_TYPE_ID[3] == Integer.parseInt(mServiceType)){
            meet = 1;
        }
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mReservationExpertsTvServiceTypeTv = (TextView) findViewById(R.id.tv_service_type_reservation_experts);
        mReservationExpertRl = (RelativeLayout) findViewById(R.id.rl_reservation_experts);
        mReservationExpertsTv = (TextView) findViewById(R.id.tv_reservation_experts);

        mExpertsListLL = (RelativeLayout) findViewById(R.id.rl_experts_list);
        mExpertsListTv = (TextView) findViewById(R.id.tv_experts_list);
        mServiceTypeRl = (RelativeLayout) findViewById(R.id.rl_service_type);
        mServiceTypeTv = (TextView) findViewById(R.id.tv_service_type);

        mAppraisalCostRL = (RelativeLayout) findViewById(R.id.rl_appraisal_cost);
        mAppraisalCostTv = (TextView) findViewById(R.id.tv_appraisal_cost);

        mCollectionTypeTv = (TextView) findViewById(R.id.tv_collection_type);

        mCollectionNumRL = (RelativeLayout) findViewById(R.id.rl_collection_num);
        mCollectionNumTv = (TextView) findViewById(R.id.tv_collection_num);

        mSpecificationIdentificationEdt = (EditText) findViewById(R.id.edt_specification_identification);
        mSubmitReservationTv = (TextView) findViewById(R.id.tv_submit_reservation);

        mMoneyDetailLL = (LinearLayout) findViewById(R.id.ll_money_detail);
        mMoneyTv = (TextView) findViewById(R.id.tv_money_to_pay);
        mModifyTv = (TextView) findViewById(R.id.tv_modify);
        mPayTv = (TextView) findViewById(R.id.tv_to_pay);

        mSubmitRl = (RelativeLayout) findViewById(R.id.rl_submit_reservation);
        mSubmitRl.setVisibility(TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY, mActivityTarget) ? View.GONE : View.VISIBLE);
        if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_EXPERT_DETAIL_ACTIVITY, mActivityTarget)) {//专家详情页进入，确定专家
            mReservationExpertsTvServiceTypeTv.setVisibility(View.VISIBLE);
            mReservationExpertsTv.setVisibility(View.GONE);

            mExpertsListLL.setVisibility(View.GONE);
            mServiceTypeRl.setVisibility(View.VISIBLE);

            mAppraisalCostRL.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_SERVICE_NOTE_ACTIVITY, mActivityTarget)) {//服务说明页进入，确定鉴定类型
            mReservationExpertsTvServiceTypeTv.setVisibility(View.GONE);
            mReservationExpertsTv.setVisibility(View.VISIBLE);
            mReservationExpertRl.setOnClickListener(this);

            mServiceTypeRl.setVisibility(View.GONE);
            mExpertsListLL.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_SERVICE_NOTE_ACTIVITY_FULL, mActivityTarget)) {//从专家详情进入，已确定鉴定类型与专家
            mReservationExpertsTvServiceTypeTv.setVisibility(View.VISIBLE);
            mReservationExpertsTv.setVisibility(View.GONE);

            mExpertsListLL.setVisibility(View.GONE);
            mServiceTypeRl.setVisibility(View.GONE);

            mAppraisalCostRL.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_ACTIVITY, mActivityTarget)) {//我的预约详情页修改,确定服务类型，鉴定类型，只能修改数量，说明
            Drawable drawable = getResources().getDrawable(R.drawable.bg_content_corner);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mReservationExpertsTv.setCompoundDrawables(drawable, null, null, null);
            mExpertsListTv.setCompoundDrawables(drawable, null, null, null);
        } else if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY, mActivityTarget)) {//查看我的预约专家详情页
            mMoneyDetailLL.setVisibility(TextUtils.equals(IntentConstant.APPOINTMENT_IDENTIFY_IDENTIFY, mAppointmentIdentifyType) ? View.VISIBLE : View.GONE);

            Drawable drawable = getResources().getDrawable(R.drawable.bg_content_corner);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            mReservationExpertsTv.setCompoundDrawables(drawable, null, null, null);
            mExpertsListTv.setCompoundDrawables(drawable, null, null, null);
//            mCollectionNumTv.setCompoundDrawables(drawable, null, null, null);

            mSpecificationIdentificationEdt.setEnabled(false);
        }

        showIdentifyNum();
        if ((AppConstant.SERVICE_TYPE_ID[4] + "").equals(mServiceType)){
            mReservationExpertsTvServiceTypeTv.setText("包含 " + mExpertName + " 等三位权威专家");
        }else{
            mReservationExpertsTvServiceTypeTv.setText(mExpertName);
        }
        mServiceTypeTv.setText(mServiceTypeName);
        mCollectionTypeTv.setText(mIdentifyTypeName);
        mAppraisalCostTv.setText(mAppraisalCost);

        refreshAppraisalCostView();
        setOnClickListener(mExpertsListLL, mCollectionNumRL, mServiceTypeRl, mSubmitReservationTv, mModifyTv, mPayTv);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {
        if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_SERVICE_NOTE_ACTIVITY, mActivityTarget) && mLatitude != 0.0 && mLontitude != 0.0) {
            NetUtils.getInstance(false)
                    .get(mContext, NetConstant.getServiceAppointmentParams(mContext, mServiceType, mIdentifyType, mLatitude, mLontitude,meet), new ServiceAppointmentListener(mContext));
        } else if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_ACTIVITY, mActivityTarget) ||
                TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY, mActivityTarget)) {
            NetUtils.getInstance(false).get(mContext, NetConstant.getEditUserServiceAppointmentParams(mContext, mAppointmentIdentifyId, mAppointmentIdentifyType),
                    new EditServiceAppointmentListener(mContext));
        }
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_back:
                showFinishPickDialog();
                break;
            case R.id.rl_reservation_experts:
                if (mIdentifyEntity==null || mIdentifyEntity.getList().size() == 0){
                    return;
                }
                if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_ACTIVITY, mActivityTarget) ||
                        TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY, mActivityTarget)) {
                    return;
                }
                mServiceAppointmentPopupWindow =
                        new ServiceAppointmentPopupWindow(ServiceAppointmentActivity.this, mReservationExpertsList, this, IntentConstant.RESERVATION_EXPERTS,
                                mReservationExpertsPosition);
                mServiceAppointmentPopupWindow.showPopupWindow(mReservationExpertsTv);
                break;
            case R.id.tv_finish:
                int position = (int) view.getTag();
                int status = (int) view.getTag(R.id.service_appointment_status);
                switch (status) {
                    case IntentConstant.RESERVATION_EXPERTS:
                        mReservationExpertsPosition = position;
                        mReservationExpertsTv.setText(mReservationExpertsList.get(position));
                        if (reservationExpertsArray[0].equals(mReservationExpertsList.get(position))){
                            mAppraisalCostTv.setText(StringUtils.getString(mIdentifyEntity.getTui().getPrice(),getString(R.string.unit_of_money),mIdentifyEntity.getTui().getTime()));
                        }else if(mIdentifyEntity.getList().size() != 0){
                            mAppraisalCostTv.setText(StringUtils.getString(mIdentifyEntity.getList().get(0).getPrice(),getString(R.string.unit_of_money),mIdentifyEntity.getList().get(0).getTime()));
                        }else{
                            mExpertsListTv.setText(getString(R.string.no_collection_experts));
                            mExpertsListTv.setTextColor(Color.RED);
                        }
                        break;
                    case IntentConstant.EXPERTS_LIST:
                        mExpertsListPosition = position;
                        if (mIdentifyEntity != null && mIdentifyEntity.getList().size() > 0) {
                            if ((AppConstant.SERVICE_TYPE_ID[4] + "").equals(mServiceType)){
                                mExpertsListTv.setText(StringUtils.getString("包含 " + mIdentifyEntity.getList().get(position).getName()) + " 等三位权威专家");
                            }else {
                                mExpertsListTv.setText(StringUtils
                                        .getString(mIdentifyEntity.getList().get(position).getOrg(), "  ", mIdentifyEntity.getList().get(position).getName()));
                                mAppraisalCostTv.setText(StringUtils.getString(mIdentifyEntity.getList().get(position).getPrice(), getString(R.string.unit_of_money), mIdentifyEntity.getList().get(position).getTime()));
                            }
                        }
                        break;
                    case IntentConstant.COLLECTION_NUM:
                        mCollectionNumPosition = position;
                        mCollectionNumTv.setText(mCollectionNumList.get(position));
                        break;
                    case IntentConstant.DETAIL_EXPERT_SERVICE_TYPE:
                        mServiceInfoPosition = position;
                        refreshServiceTypeView(mSerivceTypeIdList.get(position), mSerivceTypeNameList.get(position), mServiceTypePriceList.get(position));
                        break;
                    default:
                        break;
                }
                if (mServiceAppointmentPopupWindow != null) {
                    mServiceAppointmentPopupWindow.dismiss();
                }
                refreshAppraisalCostView();
                break;
            case R.id.rl_experts_list:
                if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_ACTIVITY, mActivityTarget) ||
                        TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY, mActivityTarget)) {
                    return;
                }
                mServiceAppointmentPopupWindow = new ServiceAppointmentPopupWindow(ServiceAppointmentActivity.this, mIdentifyEntity.getList(), this, mExpertsListPosition);
                mServiceAppointmentPopupWindow.showPopupWindow(mExpertsListLL);
                break;
            case R.id.rl_service_type:
                if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_ACTIVITY, mActivityTarget) ||
                        TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY, mActivityTarget)) {
                    return;
                }
                if (mSerivceTypeIdList != null) {
                    mServiceAppointmentPopupWindow =
                            new ServiceAppointmentPopupWindow(ServiceAppointmentActivity.this, mServiceTypeInfoList, this, IntentConstant.DETAIL_EXPERT_SERVICE_TYPE,
                                    mServiceInfoPosition);
                    mServiceAppointmentPopupWindow.showPopupWindow(mServiceTypeRl);
                }
                break;
            case R.id.rl_collection_num:
                if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY, mActivityTarget)) {
                    return;
                }
                mServiceAppointmentPopupWindow =
                        new ServiceAppointmentPopupWindow(ServiceAppointmentActivity.this, mCollectionNumList, this, IntentConstant.COLLECTION_NUM, mCollectionNumPosition);
                mServiceAppointmentPopupWindow.showPopupWindow(mCollectionNumTv);
                break;
            case R.id.tv_submit_reservation:
                //用户未登录，则在点击事件中直接跳转登录界面
                if (!UserInfoUtils.checkUserLogin(mContext)) {
                    showPickDialog(getString(R.string.not_login), getString(R.string.go_to_login_or_register), getString(R.string.negative), getString(R.string.positive),
                            mLoginSubmitListener, mCancelListener);
                    return;
                }

                if (TextUtils.isEmpty(UserInfoUtils.getPhone(mContext))) {
                    showPickDialog(getString(R.string.not_check_phone_number), getString(R.string.please_check_phone_number), getString(R.string.negative),
                            getString(R.string.positive),
                            mCheckPhoneSubmitListener, mCancelListener);
                    return;
                }
                commitOrder();//提交订单
                break;
            case R.id.tv_modify:
                if (!TextUtils.equals(IntentConstant.APPOINTMENT_IDENTIFY_IDENTIFY, mAppointmentIdentifyType)) {
                    return;
                }
                intent = new Intent(mContext, ServiceAppointmentActivity.class);
                intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_ACTIVITY);
                intent.putExtra(IntentConstant.APPOINTMENT_IDENTIFY_ID, mAppointmentIdentifyId);
                intent.putExtra(IntentConstant.APPOINTMENT_IDENTIFY_TYPE, mAppointmentIdentifyType);
                jump(intent);
                break;
            case R.id.tv_to_pay:
                if (!TextUtils.equals(IntentConstant.APPOINTMENT_IDENTIFY_IDENTIFY, mAppointmentIdentifyType)) {
                    return;
                }
                intent = new Intent(mContext, UserPayActivity.class);
                intent.putExtra(IntentConstant.USER_PAY_GOODS_ID, mAppointmentIdentifyId);
                jump(intent);
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntentConstant.RequestCodeCheckPhoneNumber:
                    boolean isPhoneChecked = data.getBooleanExtra(IntentConstant.CHECK_PHONE_FLAG, false);
                    if (isPhoneChecked) {
                        commitOrder();
                    } else {
                        DialogUtils.showShortPromptToast(mContext, R.string.check_phone_failed);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 提交订单
     */
    private void commitOrder() {
        if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_SERVICE_NOTE_ACTIVITY, mActivityTarget) && mIdentifyEntity.getList() != null && mIdentifyEntity.getList().size() > 0) {
            mExpertId = mIdentifyEntity.getList().get(mExpertsListPosition).getId();
        }
        // 启动支付
        if (!TextUtils.isEmpty(mSpecificationIdentificationEdt.getText().toString().trim()) && !TextUtils.isEmpty(mServiceType) && !TextUtils.isEmpty(mIdentifyType)) {
            String tid = TextUtils.equals(mReservationExpertsList.get(0), mReservationExpertsTv.getText().toString().trim()) ? "0" : "1";
            String size = mCollectionNumTv.getText().toString().trim();
            NetUtils.getInstance(false).postNocache(mContext, NetConstant.getOrderIdParams(mContext, tid, mServiceType, mIdentifyType, size, mExpertId,
                    mSpecificationIdentificationEdt.getText().toString().trim(), mLatitude, mLontitude), new GetOrderIdListener(mContext));
        } else {
            DialogUtils.showShortPromptToast(mContext, R.string.info_is_uncomplete);
        }
    }

    /**
     * 判断是否显示件数
     */
    private void showIdentifyNum() {
        if (TextUtils.equals(String.valueOf(AppConstant.SERVICE_TYPE_ID[0]), mServiceType) || TextUtils.equals(String.valueOf(AppConstant.SERVICE_TYPE_ID[5]), mServiceType)
                || TextUtils.equals(String.valueOf(AppConstant.SERVICE_TYPE_ID[6]), mServiceType) ||
                TextUtils.equals(String.valueOf(AppConstant.SERVICE_TYPE_ID[7]), mServiceType)) {
            mCollectionNumRL.setVisibility(View.GONE);
        } else {
            mCollectionNumRL.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 刷新鉴定费用View
     */
    private void refreshAppraisalCostView() {
        if (TextUtils.equals(getString(R.string.experts_recommend), mReservationExpertsTv.getText())) {
            mAppraisalCostRL.setVisibility(View.VISIBLE);
            mExpertsListLL.setVisibility(View.GONE);
        } else {
            mAppraisalCostRL.setVisibility(View.VISIBLE);
            mExpertsListLL.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 刷新与服务项目有关的所有View
     */
    private void refreshServiceTypeView(String serviceTypeId, String serviceName, String servicePrice) {
        mServiceType = serviceTypeId;
        mTitleView.setText(serviceName);
        mServiceTypeTv.setText(serviceName);
        mAppraisalCostTv.setText(servicePrice);
    }

    private class ServiceAppointmentListener extends NetUtils.Callback<ServiceAppointmentResponse> {

        public ServiceAppointmentListener(Context context) {
            super(context, ServiceAppointmentResponse.class);
        }

        @Override
        public void onNetSuccess(ServiceAppointmentResponse response) {
            if (response != null) {
                mAppraisalCostTv.setText(StringUtils.getString(response.getData().getTui().getPrice(), getString(R.string.unit_of_money), response.getData().getTui().getTime()));
                mIdentifyEntity = response.getData();
                if (mIdentifyEntity != null && mIdentifyEntity.getList().size() > 0) {
                    if ((AppConstant.SERVICE_TYPE_ID[4] + "").equals(mServiceType)){
                        mExpertsListTv.setText(StringUtils.getString("包含 " + mIdentifyEntity.getList().get(0).getName()) + " 等三位权威专家");
                    }else{
                        mExpertsListTv.setText(StringUtils.getString(mIdentifyEntity.getList().get(0).getOrg(), "  ", mIdentifyEntity.getList().get(0).getName()));
                    }
                }else{
                    mExpertsListTv.setText(getString(R.string.no_collection_experts));
                }
            }
        }
    }

    private class EditServiceAppointmentListener extends NetUtils.Callback<EditServiceAppointmentResponse> {

        public EditServiceAppointmentListener(Context context) {
            super(context, EditServiceAppointmentResponse.class);
        }

        @Override
        public void onNetSuccess(EditServiceAppointmentResponse response) {
            if (response != null && response.getData() != null) {
                mIdentifyType = response.getData().getKind();
                mIdentifyTypeName = response.getData().getKind_name();
                mServiceType = response.getData().getServe();
                mServiceTypeName = response.getData().getServe_name();
                mAppraisalCost = StringUtils.getString(response.getData().getPrice(), getString(R.string.unit_of_money), response.getData().getTime());

                showIdentifyNum();
//                mExpertsListLL.setVisibility(TextUtils.equals(IntentConstant.OPTIONAL_EXPERTS_ID, response.getData().getType()) ? View.VISIBLE : View.GONE);
                mAppraisalCostRL.setVisibility(TextUtils.equals(IntentConstant.OPTIONAL_EXPERTS_ID, response.getData().getType()) ? View.VISIBLE : View.GONE);

//                if (TextUtils.equals(IntentConstant.EXPERTS_RECOMMEND_ID, response.getData().getType())) {
//                    mAppraisalCostTv.setText(StringUtils.getString(response.getData().getPrice(), getString(R.string.unit_of_money), response.getData().getTime()));
//                }
                if (TextUtils.equals(IntentConstant.OPTIONAL_EXPERTS_ID, response.getData().getType()) && !TextUtils.isEmpty(response.getData().getEid())) {
                    mExpertId = response.getData().getEid();
                    mAppraisalCostTv.setText(StringUtils.getString(response.getData().getPrice(), getString(R.string.unit_of_money), response.getData().getTime()));
                }
                mTitleView.setText(mServiceTypeName);
                mReservationExpertsTv.setText(response.getData().getName());
                mCollectionTypeTv.setText(response.getData().getKind_name());
                mCollectionNumTv.setText(response.getData().getSize());
                mSpecificationIdentificationEdt.setText(response.getData().getNote());
                CharSequence mEtText = mSpecificationIdentificationEdt.getText();
                if (mEtText != null) {
                    Spannable spanText = (Spannable) mEtText;
                    Selection.setSelection(spanText, mEtText.length());
                }
                mMoneyTv.setText(response.getData().getCharge_price());
            }
        }
    }

    private class GetOrderIdListener extends NetUtils.Callback<GetOrderIdResponse> {

        public GetOrderIdListener(Context context) {
            super(context, GetOrderIdResponse.class);
        }

        @Override
        public void onNetSuccess(GetOrderIdResponse response) {
            if (response != null && response.getData() != null) {
                if (response.getError()) {
                    DialogUtils.showShortPromptToast(mContext, response.getMessage());
                    return;
                }
                // 启动支付
                Intent intent = new Intent(mContext, UserPayActivity.class);
                intent.putExtra(IntentConstant.USER_PAY_GOODS_ID, String.valueOf(response.getData().getId()));
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, mIdentifyType);
                jump(intent);
            }
        }
    }

    /**
     * 选择提示对话框
     */
    private void showFinishPickDialog() {
        if (TextUtils.equals(IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY, mActivityTarget)) {
            finish();
        }
        showPickDialog(getString(R.string.submit_quit), getString(R.string.order_no_save), getString(R.string.continue_modify), getString(R.string.confirm_cancel),
                mFinishSubmitListener, mCancelListener);
    }

    /**
     * 选择提示对话框
     */
    private void showPickDialog(String strTitle, String strContent, String strNeg, String strPos,
                                View.OnClickListener submitListener, View.OnClickListener cancelListener) {
        mCommonDialog = DialogUtils.showCommonDialog(mContext, strTitle, strContent, strNeg, strPos, submitListener, cancelListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showFinishPickDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
