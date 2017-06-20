package com.bobao.identifypro.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.bobao.identifypro.constant.AppConstant;

/**
 * Created by star on 16/2/29.
 */
public class MPermissionUtil {
    public static void requestPermissionStorage(Activity activity) {
        //判断 --6.0以上 该功能需要读写存储权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0运行时权限适配
            boolean isReadStorageGranted = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            boolean isWriteStorageGranted = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if(!isReadStorageGranted || !isWriteStorageGranted) {
                //没有授权, 申请授权
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstant.PERMISSION_REQ_CODE_READ_STORAGE);
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstant.PERMISSION_REQ_CODE_WRITE_STORAGE);
            }
        }
    }

    public static void requestPermissionCamera(Activity activity) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0运行时权限适配
            boolean isCameraGranted = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            if(!isCameraGranted) {
                //没有授权, 申请授权
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, AppConstant.PERMISSION_REQ_CODE_READ_CAMERA);
            }else{
                //已授权，就直接申请读写存储权限
                requestPermissionStorage(activity);
            }
        }
    }
}
