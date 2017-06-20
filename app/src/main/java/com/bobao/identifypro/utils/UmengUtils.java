package com.bobao.identifypro.utils;

import android.content.Context;

import com.bobao.identifypro.constant.EventEnum;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class UmengUtils {

    public static void init(Context context) {
//        MobclickAgent.updateOnlineConfig(context);
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    /**
     * 页面resume
     */
    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    /**
     * 页面pause
     */
    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    /**
     * 统计页面起始时间
     */
    public static void onPageStart(String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    /**
     * 统计页面结束时间
     */
    public static void onPageEnd(String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }

    /**
     * 统计单个事件
     */
    public static void onEvent(Context context, EventEnum eventId) {
        onEvent(context, eventId.getName());
    }

    /**
     * 统计组合事件
     */
    public static void onEvent(Context context, EventEnum eventId, Map<String, String> map) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.putAll(map);
        onEvent(context, eventId.getName(), hashMap);
    }

    /**
     * 统计组合事件，并且统计出来数值
     * @param context
     * @param eventId
     * @param map
     * @param value
     */
    public static void onEvent(Context context,EventEnum eventId,Map<String,String> map,int value){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.putAll(map);
        onEvent(context,eventId.getName(),hashMap,value);
    }
    /**
     * 统计单个事件
     */
    private static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    /**
     * 统计组合事件
     */
    private static void onEvent(Context context, String eventId, HashMap<String, String> map) {
        MobclickAgent.onEvent(context, eventId, map);
    }

    /**
     * 统计组合事件，计算事件
     * @param context
     * @param eventId
     * @param map
     * @param value
     */
    private static void onEvent(Context context,String eventId,HashMap<String,String> map,int value){
        MobclickAgent.onEventValue(context, eventId, map, value);
    }
}
