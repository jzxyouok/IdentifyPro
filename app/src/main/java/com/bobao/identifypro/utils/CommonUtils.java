package com.bobao.identifypro.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by you on 2015/5/26.
 */
public class CommonUtils {
    /**
     * 手机号合法性校验
     * @param value
     * @return
     */
    public static boolean checkPhoneNumber(String value){
        String regExp = "^[1]([3][0-9]{1}|45|([5][0-9]{1})|75|76|77|([8][0-9]{1}))[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
        return m.find();
    }
}
