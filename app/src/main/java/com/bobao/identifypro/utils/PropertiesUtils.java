package com.bobao.identifypro.utils;


import android.content.Context;

import com.bobao.identifypro.constant.IntentConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static PropertiesUtils sInstance = new PropertiesUtils();

    private PropertiesUtils() {
    }

    public static PropertiesUtils getsInstance() {
        return sInstance;
    }

    public void saveProperties(Context context, Properties properties) {
        if (context == null) {
            return;
        }
        String PROP_PATH = StringUtils.getString(context.getApplicationContext().getFilesDir().getAbsolutePath(), File.separator, IntentConstant.USER_INFO_PROPERTIES_FILE);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(PROP_PATH, false);
            properties.store(fileOutputStream, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProperties(Context context, String key, String value) {
        if (context == null) {
            return;
        }
        String PROP_PATH = StringUtils.getString(context.getApplicationContext().getFilesDir().getAbsolutePath(), File.separator, IntentConstant.USER_INFO_PROPERTIES_FILE);
        Properties properties = new Properties();
        try {
            InputStream in = new FileInputStream(PROP_PATH);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        properties.setProperty(key, value);
        OutputStream fos;
        try {
            fos = new FileOutputStream(PROP_PATH);
            properties.store(fos, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String loadProperties(Context context, String key, String def) {
        if (context == null) {
            return def;
        }
        String PROP_PATH = StringUtils.getString(context.getApplicationContext().getFilesDir().getAbsolutePath(), File.separator, IntentConstant.USER_INFO_PROPERTIES_FILE);
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(PROP_PATH);
            properties.load(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties.getProperty(key, def);
    }

    public void logout(Context context) {
        if (context == null) {
            return;
        }
        String PROP_PATH = StringUtils.getString(context.getApplicationContext().getFilesDir().getAbsolutePath(), File.separator, IntentConstant.USER_INFO_PROPERTIES_FILE);
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(PROP_PATH);
            properties.load(fileInputStream);
            properties.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
