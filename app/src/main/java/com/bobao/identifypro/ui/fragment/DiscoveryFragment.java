package com.bobao.identifypro.ui.fragment;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.ui.activity.ExpertListActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.SharedPreferencesUtils;
import com.bobao.identifypro.utils.UmengUtils;

/**
 * Created by star on 15/5/29.
 */
public class DiscoveryFragment extends BaseFragment {
    private static final String SP_KEY_INNER_GUIDER = "inner_guider";

    @Override
    protected void getArgumentsData() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.fragment_identify;
    }

    @Override
    protected void initContent() {
        View bronzeView = mRootView.findViewById(R.id.iv_take_picture_bronze);
        View chinaView = mRootView.findViewById(R.id.iv_take_picture_china);
        View woodenView = mRootView.findViewById(R.id.iv_take_picture_wooden);
        View jadeView = mRootView.findViewById(R.id.iv_take_picture_jade);
        View sundryView = mRootView.findViewById(R.id.iv_take_picture_sundry);
        View paintingView = mRootView.findViewById(R.id.iv_take_picture_painting);
        final View moneyView = mRootView.findViewById(R.id.iv_take_picture_money);
        setOnClickListener(bronzeView, chinaView, woodenView, jadeView, sundryView, paintingView, moneyView);
        final View innerGuiderView = mRootView.findViewById(R.id.img_inner_guider);
        boolean innerGuiderFlag = SharedPreferencesUtils.getSharedPreferencesBoolean(mContext, SP_KEY_INNER_GUIDER);
        innerGuiderView.setVisibility(innerGuiderFlag ? View.GONE : View.VISIBLE);
        innerGuiderView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SharedPreferencesUtils.setSharedPreferencesBoolean(mContext, SP_KEY_INNER_GUIDER, true);
                innerGuiderView.setVisibility(View.GONE);
                return true;
            }
        });
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, ExpertListActivity.class);
        intent.putExtra(IntentConstant.EXPERT_LIST_ENTRANCE, IntentConstant.EXPERT_LIST);
        switch (view.getId()) {
            case R.id.iv_icon:
                showDebugInfo(mContext, "");
                break;
            case R.id.iv_take_picture_bronze:
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeBronze);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[3]);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageBronze);
                break;
            case R.id.iv_take_picture_china:
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeChina);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[0]);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageChina);
                break;
//            case R.id.iv_take_picture_wooden:
//                 intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeWooden);
//                 intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[3]);
//                 ActivityUtils.jump(mContext, intent);
//                 UmengUtils.onEvent(mContext, EventEnum.IdentifyPageWooden);
//                break;
            case R.id.iv_take_picture_jade:
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeJade);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[1]);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageJade);
                break;
            case R.id.iv_take_picture_sundry:
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeSundry);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[5]);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageSundry);
                break;
            case R.id.iv_take_picture_painting:
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypePainting);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[2]);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPagePainting);
                break;
            case R.id.iv_take_picture_money:
//                DialogUtils.showChooseDialog(mContext);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeMoney);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[4]);
                ActivityUtils.jump(mContext, intent);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyPageMoney);
                break;
            default:
                super.onClick(view);
                break;
        }
    }
}
