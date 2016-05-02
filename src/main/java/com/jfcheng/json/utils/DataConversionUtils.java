package com.jfcheng.json.utils;

/**
 * Created by jfcheng on 4/27/16.
 */
public class DataConversionUtils {
    private DataConversionUtils() {
    }


    public static Object stringToNumber(String strValue, Class clazz) {
        Number number = null;
        if (strValue.contains(".") || strValue.contains("e") || strValue.contains("E")) {
            number = Double.valueOf(strValue);
        } else {
            number = Long.valueOf(strValue);
        }
        return numberToPrimitive(number, clazz);
    }

    public static Object numberToPrimitive(Number num, Class clazz) {
        if (clazz == Long.class || clazz == Long.TYPE) {
            return num.longValue();
        } else if (clazz == Integer.class || clazz == Integer.TYPE) {
            return num.intValue();
        } else if (clazz == Short.class || clazz == Short.TYPE) {
            return num.shortValue();
        } else if (clazz == Double.class || clazz == Double.TYPE) {
            return num.doubleValue();
        } else if (clazz == Float.class || clazz == Float.TYPE) {
            return num.floatValue();
        } else if (clazz == Byte.class || clazz == Byte.TYPE) {
            return num.byteValue();
        } else if (clazz == String.class) {
            return String.valueOf(num);
        } else {
            return num;
        }
    }

    public static boolean isNumberType(Class clazz) {
        if (Number.class.isAssignableFrom(clazz) || (clazz.isPrimitive() && clazz != Character.TYPE && clazz != Boolean.TYPE)) {
            return true;
        } else {
            return false;
        }
    }
}
