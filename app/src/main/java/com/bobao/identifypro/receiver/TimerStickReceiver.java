package com.bobao.identifypro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.ui.activity.SplashActivity;
import com.bobao.identifypro.utils.NotificationUtils;
import com.bobao.identifypro.utils.SharedPreferencesUtils;
import com.bobao.identifypro.utils.UserInfoUtils;

/**
 * Created by kakaxicm on 2015/11/5.
 */
public class TimerStickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (SharedPreferencesUtils.getSharedPreferencesBoolean(context, AppConstant.HAS_NOCHARGE_IDENTIFY)
                && UserInfoUtils.checkUserLogin(context)){
            intent = new Intent(context, SplashActivity.class);
            intent.putExtra(IntentConstant.IS_FROM_NOTIFICATION, true);
            intent.putExtra(IntentConstant.IDENTIFY_PAGE_INDEX, IntentConstant.IDENTIFY_PAGE_INDEX_NO_PAY);
            NotificationUtils.sendNotification(context, context.getString(R.string.app_name), context.getString(R.string.no_charge_tip), intent);
        }
    }
}
