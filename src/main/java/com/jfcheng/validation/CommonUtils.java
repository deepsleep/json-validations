package com.jfcheng.validation;

/**
 * Created by jfcheng on 2/29/16.
 */
public class CommonUtils {

    private CommonUtils() {
    }

    public static boolean isNullOrEmptyString(String str) {
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
