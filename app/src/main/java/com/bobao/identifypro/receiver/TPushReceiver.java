package com.bobao.identifypro.receiver;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bobao.identifypro.domain.XGNotification;
import com.bobao.identifypro.service.NotificationService;
import com.bobao.identifypro.utils.DeviceUtil;
import com.bobao.identifypro.utils.DialogUtils;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TPushReceiver extends XGPushBaseReceiver {
    public static final String TAG = TPushReceiver.class.getSimpleName();
    private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");

    private void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 收到推送消息调用
     */
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult notifyShowedResult) {
        if (context == null || notifyShowedResult == null) {
            return;
        }
        XGNotification notification = new XGNotification();
        // 获取通知id
        notification.setMsg_id(notifyShowedResult.getMsgId());
        // 设置通知标题
        notification.setTitle(notifyShowedResult.getTitle());
        // 设置通知内容
        notification.setContent(notifyShowedResult.getContent());
        // notificationActionType==1为Activity，2为url，3为intent
        notification.setNotificationActionType(notifyShowedResult.getNotificationActionType());
        // Activity,url,intent都可以通过getActivity()获得
        notification.setActivity(notifyShowedResult.getActivity());
        // 设置时间
        notification.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        // 数据库保存收到的通知
        NotificationService.getInstance(context).save(notification);
        // 发送通知
        context.sendBroadcast(intent);
        // 显示数据
        if (DeviceUtil.isApkDebugable(context)) {
            DialogUtils.showLongPromptToast(context, "您有1条新消息, " + "通知被展示 ， \n" + notifyShowedResult.toString());
        }
    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
//        if (context == null) {
//            return;
//        }
//        String text;
//        if (errorCode == XGPushBaseReceiver.SUCCESS) {
//            text = "反注册成功";
//        } else {
//            text = "反注册失败" + errorCode;
//        }
//        Log.d(TAG, text);
//        show(context, text);
    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
//        if (context == null) {
//            return;
//        }
//        String text = "";
//        if (errorCode == XGPushBaseReceiver.SUCCESS) {
//            text = "\"" + tagName + "\"设置成功";
//        } else {
//            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
//        }
//        Log.d(TAG, text);
//        show(context, text);
    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
//        if (context == null) {
//            return;
//        }
//        String text = "";
//        if (errorCode == XGPushBaseReceiver.SUCCESS) {
//            text = "\"" + tagName + "\"删除成功";
//        } else {
//            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
//        }
//        Log.d(TAG, text);
//        show(context, text);
    }

    /**
     * 通知栏里面通知被点击或者被消除调用
     */
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult message) {
//        if (context != null && message != null) {
//            String text = "";
//            if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
//                // 通知在通知栏被点击啦。。。。。
//                // APP自己处理点击的相关动作
//                // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
//                text = "通知被打开 :" + message;
//            } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
//                // 通知被清除啦。。。。
//                // APP自己处理通知被清除后的相关动作
//                text = "通知被清除 :" + message;
//            }
//            Toast.makeText(context, "广播接收到通知被点击:" + message.toString(), Toast.LENGTH_SHORT).show();
//            // 获取自定义key-value
//            String customContent = message.getCustomContent();
//            if (customContent != null && customContent.length() != 0) {
//                try {
//                    JSONObject obj = new JSONObject(customContent);
//                    // key1为前台配置的key
//                    if (!obj.isNull("key")) {
//                        String value = obj.getString("key");
//                        Log.d(TAG, "get custom value:" + value);
//                    }
//                    // ...
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            // APP自主处理的过程。。。
//            Log.d(TAG, text);
//            show(context, text);
//        }
    }

    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {
//        // TODO Auto-generated method stub
//        if (context == null || message == null) {
//            return;
//        }
//        String text = "";
//        if (errorCode == XGPushBaseReceiver.SUCCESS) {
//            text = message + "注册成功";
//            // 在这里拿token
//            String token = message.getToken();
//            Log.e("token----", token);
//        } else {
//            text = message + "注册失败，错误码：" + errorCode;
//        }
//        Log.d(TAG, text);
//        show(context, text);
    }

    // 消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
//        // TODO Auto-generated method stub
//        String text = "收到消息:" + message.toString();
//        // 获取自定义key-value
//        String customContent = message.getCustomContent();
//        if (customContent != null && customContent.length() != 0) {
//            try {
//                JSONObject obj = new JSONObject(customContent);
//                // key1为前台配置的key
//                if (!obj.isNull("key")) {
//                    String value = obj.getString("key");
//                    Log.d(TAG, "get custom value:" + value);
//                }
//                // ...
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        // APP自主处理消息的过程...
//        Log.d(TAG, text);
//        show(context, text);
    }
}
