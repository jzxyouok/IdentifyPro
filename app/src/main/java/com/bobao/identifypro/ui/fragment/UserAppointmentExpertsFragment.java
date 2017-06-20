package com.bobao.identifypro.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.application.IdentifyProApplication;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.NormalMessageResponse;
import com.bobao.identifypro.domain.UserAppointmentExpertsResponse;
import com.bobao.identifypro.ui.activity.MainActivity;
import com.bobao.identifypro.ui.activity.PriceQueryContentActivity;
import com.bobao.identifypro.ui.activity.ServiceAppointmentActivity;
import com.bobao.identifypro.ui.activity.UserLogInActivity;
import com.bobao.identifypro.ui.activity.UserPayActivity;
import com.bobao.identifypro.ui.dialog.CommonDialog;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;

import java.util.LinkedList;
import java.util.List;

public class UserAppointmentExpertsFragment extends BasePagerLoadListViewFragment {
    private static final String IS_PUBLIC_ZERO = "0";
    private static final String IS_PUBLIC_ONE = "1";
    private static final String IDENTIFY_TYPE = "IDENTIFY_TYPE";
    //    type:0全部 1未支付 2已支付未鉴定 3已鉴定
    private int mType;
    private List<UserAppointmentExpertsResponse.DataEntity> mData;

    public static UserAppointmentExpertsFragment creatUserIdentifyFragment(int type) {
        UserAppointmentExpertsFragment fragment = new UserAppointmentExpertsFragment();
        Bundle arg = new Bundle();
        arg.putInt(IDENTIFY_TYPE, type);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    protected void loadData() {//用户鉴定刷新页面
        mCurrentPage = 1;
        startPageLoadRequest(mCurrentPage);
    }

    private boolean mTypeFlags = false;

    @Override
    protected BaseAdapter getAdapter() {
        return new UserIdentifyAdapter();
    }

    @Override
    protected RequestParams configNetRequestParams() {
        mType = getArguments().getInt(IDENTIFY_TYPE, 1);
        return NetConstant.getUserAppointmentExpertsParams(mContext, String.valueOf(mType));
    }

    @Override
    protected String configUrl() {
        return NetConstant.HOST;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void startNetPagerDataRequest(final int pageIndex) {
        if (pageIndex == 1) {
            mLoadFailedUI.setVisibility(View.GONE);
        }
        NetUtils.getInstance(false).getNoCache(mContext, mParams, new UserIdentifyListener(mContext, pageIndex));
    }

    private class UserIdentifyAdapter extends BaseAdapter implements View.OnClickListener {
        private CommonDialog mCommonDialog;

        @Override
        public int getCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            if (mData != null) {
                return mData.get(i);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_user_appointment_experts, null);

                holder.line = convertView.findViewById(R.id.line);
                holder.ll_layout = (LinearLayout) convertView.findViewById(R.id.ll_layout);

                holder.sdv_server_type_img = (SimpleDraweeView) convertView.findViewById(R.id.sdv_server_type_img);
                holder.tv_item_server_type = (TextView) convertView.findViewById(R.id.tv_item_server_type);
                holder.tv_item_identify_pay_num = (TextView) convertView.findViewById(R.id.tv_item_identify_pay_num);
                holder.btn_refund_identified = (TextView) convertView.findViewById(R.id.btn_refund_identified);

                holder.sdv_item_server_img = (SimpleDraweeView) convertView.findViewById(R.id.sdv_item_server_img);
                holder.tv_item_expert_honor = (TextView) convertView.findViewById(R.id.tv_item_expert_honor);
                holder.tv_item_expert_name = (TextView) convertView.findViewById(R.id.tv_item_expert_name);
                holder.tv_item_server_note = (TextView) convertView.findViewById(R.id.tv_item_server_note);

                holder.fl_layout = convertView.findViewById(R.id.fl_layout);
                holder.ll_no_pay_identify = convertView.findViewById(R.id.ll_no_pay_identify);
                holder.ll_switch_identified = convertView.findViewById(R.id.ll_switch_identified);
                holder.ll_time = convertView.findViewById(R.id.ll_time);

                holder.btn_delete_identify = convertView.findViewById(R.id.btn_delete_identify);
                holder.btn_edit_identify = convertView.findViewById(R.id.btn_edit_identify);
                holder.btn_pay_identify = convertView.findViewById(R.id.btn_pay_identify);

                holder.btn_delete_identified = convertView.findViewById(R.id.btn_delete_identified);
                holder.btn_evaluate_identified = (TextView) convertView.findViewById(R.id.btn_evaluate_identified);

                holder.tv_timeout = (TextView) convertView.findViewById(R.id.tv_timeout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            UserAppointmentExpertsResponse.DataEntity dataEntity = mData.get(i);

            holder.line.setVisibility(i == 0 ? View.GONE : View.VISIBLE);
            holder.fl_layout.setVisibility(mType == 2 ? View.GONE : View.VISIBLE);
            holder.ll_no_pay_identify.setVisibility(mType == 1 ? View.VISIBLE : View.GONE);
            holder.ll_time.setVisibility(mType == 2 ? View.VISIBLE : View.GONE);
            holder.ll_switch_identified.setVisibility(mType == 3 ? View.VISIBLE : View.GONE);
            holder.tv_item_identify_pay_num.setVisibility(mType == 3 ? View.GONE : View.VISIBLE);
            holder.tv_item_identify_pay_num.setTextColor(mType == 1 ? getResources().getColor(R.color.red_65) : getResources().getColor(R.color.black3));
            holder.btn_refund_identified.setVisibility(mType == 3 && dataEntity.getRefund() != null && dataEntity.getRefund().size() > 0 ? View.VISIBLE : View.GONE);

            if (dataEntity != null) {
                if (!TextUtils.isEmpty(dataEntity.getType_img())) {
                    holder.sdv_server_type_img.setImageURI(Uri.parse(dataEntity.getType_img()));
                }
                holder.tv_item_server_type.setText(dataEntity.getServe());
                holder.tv_item_identify_pay_num.setText(StringUtils.getString(getString(R.string.identify_id), dataEntity.getId()));
                if (!TextUtils.isEmpty(dataEntity.getHead_img())) {
                    holder.sdv_item_server_img.setImageURI(Uri.parse(dataEntity.getHead_img()));
                }
                holder.tv_item_expert_honor.setText(dataEntity.getHonors());
                holder.tv_item_expert_name.setText(dataEntity.getName());
                holder.tv_item_server_note.setText(dataEntity.getNote());

                //设置评价
                holder.btn_evaluate_identified.setText(dataEntity.getIscomment() != 1 ? R.string.evaluate_to_expert : R.string.already_evaluate_to_expert);
                holder.btn_evaluate_identified.setTextColor(dataEntity.getIscomment() != 1 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.black3));
                holder.btn_evaluate_identified
                        .setBackgroundResource(dataEntity.getIscomment() != 1 ? R.drawable.bg_button_corner_4_orange_yellow : R.drawable.bg_content_corner_gray9);

                holder.tv_timeout.setText(dataEntity.getApply_time());

                holder.btn_delete_identify.setTag(i);//未支付删除按钮
                holder.btn_delete_identify.setOnClickListener(this);
                holder.btn_edit_identify.setTag(i);//修改按钮
                holder.btn_edit_identify.setOnClickListener(this);
                holder.btn_pay_identify.setTag(i);//支付按钮
                holder.btn_pay_identify.setOnClickListener(this);
                holder.btn_delete_identified.setTag(i);//已完成删除按钮
                holder.btn_delete_identified.setOnClickListener(this);
                holder.btn_evaluate_identified.setTag(i);//已完成评价按钮
                holder.btn_evaluate_identified.setOnClickListener(this);
                holder.btn_refund_identified.setTag(i);//有退款
                holder.btn_refund_identified.setOnClickListener(this);
                holder.ll_layout.setTag(R.id.tag_position, i);//查看详情
                holder.ll_layout.setOnClickListener(this);
            }
            return convertView;
        }

        @Override
        public void onClick(View v) {
            final int pos;
            Intent intent;
            UserAppointmentExpertsResponse.DataEntity dataEntity;
            switch (v.getId()) {
                case R.id.btn_delete_identify://待支付删除
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    mCommonDialog = DialogUtils.showCommonDialog(mContext, getString(R.string.prompt), getString(R.string.confirm_delete), getString(R.string.negative), getString
                            (R.string.positive), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCommonDialog.dismiss();
                            startDeleteIdentifyTreasureRequest(pos);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCommonDialog.dismiss();
                        }
                    });
                    break;
                case R.id.btn_edit_identify://修改
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    intent = new Intent(mContext, ServiceAppointmentActivity.class);
                    intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_ACTIVITY);//我的预约详情页修改,确定服务类型，鉴定类型，只能修改数量，说明
                    intent.putExtra(IntentConstant.APPOINTMENT_IDENTIFY_ID, mData.get(pos).getId());
                    intent.putExtra(IntentConstant.APPOINTMENT_IDENTIFY_TYPE, String.valueOf(mType));
                    jump(intent);
                    break;
                case R.id.btn_pay_identify://支付页面
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    intent = new Intent(getActivity(), UserPayActivity.class);
                    intent.putExtra(IntentConstant.USER_PAY_GOODS_ID, mData.get(pos).getId());
                    jump(intent);
                    break;
                case R.id.btn_delete_identified://已完成删除
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    mCommonDialog = DialogUtils.showCommonDialog(mContext, getString(R.string.prompt), getString(R.string.confirm_delete), getString(R.string.negative), getString
                            (R.string.positive), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCommonDialog.dismiss();
                            startDeleteIdentifiedTreasureRequest(pos);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCommonDialog.dismiss();
                        }
                    });
                    break;
                case R.id.btn_evaluate_identified://评价
                    //对专家进行评价
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    dataEntity = mData.get(pos);
                    if (dataEntity.getIscomment() != 1) {
                        IdentifyProApplication.setIntentData(IntentConstant.IS_MY_ORDER_FLAGS, true);
                        IdentifyProApplication.setIntentData(IntentConstant.QUERY_EXPERT_NAME, dataEntity.getName());
                        IdentifyProApplication.setIntentData(IntentConstant.QUERY_EXPERT_HONOR, dataEntity.getNote());
                        IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOODS_ID, dataEntity.getId());
                        IdentifyProApplication.setIntentData(IntentConstant.QUERY_GOODS_PHOTO, dataEntity.getHead_img());
                        //  2015/9/29 获取专家id，并传入评价页
                        IdentifyProApplication.setIntentData(IntentConstant.QUERY_EXPERT_ID, dataEntity.getEid());
                        IdentifyProApplication.setIntentData(IntentConstant.IS_MY_ORDER_FLAGS, true);
                        if (UserInfoUtils.checkUserLogin(mContext)) {
                            //  2015/10/9 刷新UI
                            notifyDataSetChanged();
                            jump(mContext, PriceQueryContentActivity.class);
                        } else {
                            intent = new Intent(mContext, UserLogInActivity.class);
                            jump(intent);
                        }
                    }
                    break;
                case R.id.btn_refund_identified://有退款
                    pos = (int) v.getTag();
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    dataEntity = mData.get(pos);
                    StringBuilder stringBuilder = new StringBuilder();
                    if (dataEntity.getRefund() != null && dataEntity.getRefund().size() > 0) {
                        for (int i = 0; i < dataEntity.getRefund().size(); i++) {
                            stringBuilder.append(dataEntity.getRefund().get(i).getName()).append(",").append(dataEntity.getRefund().get(i).getTime()).append(";");
                        }
                        if (stringBuilder.length() > 0) {
                            String content = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
                            DialogUtils.showRefundDetailDialog(mContext, content);
                        }
                    }
                    break;
                case R.id.ll_layout://查看详情
                    pos = (int) v.getTag(R.id.tag_position);
                    if (mData == null || mData.size() == 0 || mData.get(pos) == null) {
                        return;
                    }
                    intent = new Intent(mContext, ServiceAppointmentActivity.class);
                    intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_APPOINTMENT_EXPERT_DETAIL_ACTIVITY);
                    intent.putExtra(IntentConstant.APPOINTMENT_IDENTIFY_ID, mData.get(pos).getId());
                    intent.putExtra(IntentConstant.APPOINTMENT_IDENTIFY_TYPE, String.valueOf(mType));
                    jump(intent);
                    break;
                default:
                    break;
            }
        }

        private class ViewHolder {
            View line;

            LinearLayout ll_layout;

            SimpleDraweeView sdv_server_type_img;
            TextView tv_item_server_type;
            TextView tv_item_identify_pay_num;
            TextView btn_refund_identified;

            SimpleDraweeView sdv_item_server_img;
            TextView tv_item_expert_honor;
            TextView tv_item_expert_name;
            TextView tv_item_server_note;

            View fl_layout;//操作
            View ll_no_pay_identify;//未支付需要显示的控件
            View ll_switch_identified;//已完成设置开关
            View ll_time;//进行中的时间显示

            View btn_delete_identify;
            View btn_edit_identify;
            View btn_pay_identify;

            View btn_delete_identified;
            TextView btn_evaluate_identified;

            TextView tv_timeout;
        }
    }

    /**
     * 未支付删除
     */
    private void startDeleteIdentifyTreasureRequest(final int pos) {
        UserAppointmentExpertsResponse.DataEntity dataEntity = mData.get(pos);
        NetUtils.getInstance(false)
                .getNoCache(mContext, NetConstant.getDeleteNoPayAppointmentTreasureParams(mContext, dataEntity.getId()), new DeleteIdentifyTreasureListener(mContext, pos));
    }

    /**
     * 已完成删除
     */
    private void startDeleteIdentifiedTreasureRequest(final int pos) {
        UserAppointmentExpertsResponse.DataEntity dataEntity = mData.get(pos);
        NetUtils.getInstance(false)
                .getNoCache(mContext, NetConstant.getDeleteFinishAppointmentTreasureParams(mContext, dataEntity.getId()), new DeleteIdentifyTreasureListener(mContext, pos));
    }

    private class UserIdentifyListener extends NetUtils.Callback<UserAppointmentExpertsResponse> {

        private int pageIndex;

        public UserIdentifyListener(Context context, int pageIndex) {
            super(context, UserAppointmentExpertsResponse.class);
            this.pageIndex = pageIndex;
        }

        @Override
        public void onConvertFailed(String json) {
            mSwipyRefreshLayout.setRefreshing(false);
            mLoadingUI.setVisibility(View.GONE);
            if (pageIndex == 1) {
                handleNoDataUI();
            }
        }

        @Override
        public void onNetSuccess(UserAppointmentExpertsResponse data) {
            if (pageIndex == 1) {
                mLoadingUI.setVisibility(View.GONE);
                if (data == null || data.getError() || data.getData() == null || data.getData().size() == 0) {
                    handleNoDataUI();
                    return;
                }
                mLoadFailedUI.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
            List<UserAppointmentExpertsResponse.DataEntity> tempData = data.getData();
            mCurrentPage = pageIndex;
            if (mData == null) {
                mData = new LinkedList<>();
            }
            if (mCurrentPage == 1) {
                mData.clear();
            }
            if (tempData.size() > 0) {
                mData.addAll(tempData);
            }
            updateContentUI();
            mSwipyRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            mSwipyRefreshLayout.setRefreshing(false);
            mLoadingUI.setVisibility(View.GONE);
            if (pageIndex == 1) {
                handleLoadFailedUI();
            }
        }
    }

    private class DeleteIdentifyTreasureListener extends NetUtils.Callback<NormalMessageResponse> {
        private int pos;

        public DeleteIdentifyTreasureListener(Context context, int pos) {
            super(context, NormalMessageResponse.class);
            this.pos = pos;
        }

        @Override
        public void onNetSuccess(NormalMessageResponse response) {
            if (response == null) {
                if (getActivity() != null) {
                    DialogUtils.showLongPromptToast(mContext, R.string.delete_collection_failure);
                }
                return;
            }
            if (!response.getError()) {
                mData.remove(pos);
                updateContentUI();
                if (getActivity() != null) {
                    DialogUtils.showLongPromptToast(mContext, R.string.delete_collection_success);
                }
            } else {
                if (getActivity() != null) {
                    DialogUtils.showLongPromptToast(mContext, R.string.delete_collection_failure);
                }
            }
        }

        @Override
        public void onConvertFailed(String json) {
            if (getActivity() != null) {
                DialogUtils.showLongPromptToast(mContext, R.string.delete_collection_failure);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (getActivity() != null) {
                DialogUtils.showLongPromptToast(mContext, R.string.delete_collection_failure);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void handleNoDataUI() {
        mLoadFailedUI.setVisibility(View.VISIBLE);
        mLoadFailedTipTv.setText(R.string.no_related_order);
        mLoadFailedBtn.setVisibility(View.VISIBLE);
        mLoadFailedBtn.setText(R.string.our_indicated);
        mLoadFailedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.tv_home);
                jump(intent);
                UmengUtils.onEvent(mContext, EventEnum.User_Identify_Nodate_Want_Identify);
            }
        });
    }
}
