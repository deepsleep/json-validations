package com.jfcheng.json.annotation;

import com.jfcheng.json.annotation.exception.InvalidParameterValueException;
import com.jfcheng.json.annotation.exception.RequiredFieldNotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jfcheng on 2/28/16.
 */
public class AnnotationHelper {

    private AnnotationHelper() {
    }

    public static String getFieldName(Field f) {
        JsonName jsonName = f.getAnnotation(JsonName.class);
        if (jsonName != null && jsonName.value() != null) {
            return jsonName.value();
        } else {
            return f.getName();
        }
    }

    public static boolean isEntityToJsonIgnore(Field f) {
        EntityToJsonIgnore ignore = f.getAnnotation(EntityToJsonIgnore.class);
        if (ignore != null) {
            return ignore.value();
        } else {
            return false;
        }
    }

    public static boolean isJsonToEntityIgnore(Field f){
        JsonToEntityIgnore ignore = f.getAnnotation(JsonToEntityIgnore.class);
        if(ignore !=null){
            return ignore.value();
        }else{
            return false;
        }
    }


    public static void doRequiredAnnotationValidation(String fieldName, Object value, Annotation[] annotations) throws RequiredFieldNotFoundException {
        if (value == null && annotations != null) {
            for (Annotation a : annotations) {
                if (a instanceof Required) {
                    if (((Required) a).value() == true) {
                        throw new RequiredFieldNotFoundException("Field [ " + fieldName + " ] is required.");
                    }
                }
            }

        }
    }

    public static void doNumberAnnotationValidation(String fieldName, Object value, Annotation[] annotations) throws InvalidParameterValueException {
        if (annotations != null) {
            Number numberValue = (Number) value;
            long num = numberValue.longValue();
            for (Annotation a : annotations) {
                if (a instanceof MinValue) {
                    doMinValidation(fieldName, num, ((MinValue) a).value(), false);
                }

                if (a instanceof MaxValue) {
                    doMaxValidation(fieldName, num, ((MaxValue) a).value(), false);
                }

                if (a instanceof RangeValue) {
                    doRangeValueValidation(fieldName, num, ((RangeValue) a).min(), ((RangeValue) a).max());
                }

                if (a instanceof ValidNumbers) {
                    doValidNumberValidation(fieldName, num, ((ValidNumbers) a).value());
                }
            }

        }
    }

    public static void doStringAnnotationValidation(String fieldName, Object value, Annotation[] annotations) throws InvalidParameterValueException {
        if (annotations != null) {
            String str = (String) value;
            int size = str.length();
            for (Annotation a : annotations) {
                if (a instanceof ValidLength) {
                    doSizeValidation(fieldName, size, ((ValidLength) a).min(), ((ValidLength) a).max(), true);
                }

                if (a instanceof ValidStrings) {
                    doValidStringValidation(fieldName, str, ((ValidStrings) a).value());
                }

                if( a instanceof  StringRegex){
                    doValidStringRegexValidation(fieldName,str, ((StringRegex) a).value(),((StringRegex) a).name());
                }
            }
        }
    }

    public static void doCollectionAnnotationValidation(String fieldName, Object value, Annotation[] annotations) throws InvalidParameterValueException {
        if (annotations != null) {
            int size = -1;
            if (value.getClass().isArray()) {
                size = Array.getLength(value);
            } else if (value instanceof Collection) {
                size = ((Collection) value).size();
            } else if (value instanceof Map) {
                size = ((Map) value).size();
            }

            for (Annotation a : annotations) {
                if (a instanceof ValidLength) {
                    doSizeValidation(fieldName, size, ((ValidLength) a).min(), ((ValidLength) a).max(), true);
                }
            }
        }
    }


    private static void doMinValidation(String fieldName, long value, long min, boolean isLength) throws InvalidParameterValueException {
        if (value < min) {
            if (isLength) {
                throw new InvalidParameterValueException("Invalid length of field [ " + fieldName + " ] : length " + value + " is less than min length " + min);
            } else {
                throw new InvalidParameterValueException("Field [ " + fieldName + " ] : value " + value + " is less than min value " + min);
            }
        }
    }

    private static void doMaxValidation(String fieldName, long value, long max, boolean isLength) throws InvalidParameterValueException {
        if (value > max) {
            if (isLength) {
                throw new InvalidParameterValueException("Invalid length of field [ " + fieldName + " ] : length " + value + " is greater than max length " + max);
            } else {
                throw new InvalidParameterValueException("Field [ " + fieldName + " ] : value " + value + " is greater than max value " + max);
            }
        }
    }

    private static void doRangeValueValidation(String fieldName, long value, long min, long max) throws InvalidParameterValueException {
        doMaxValidation(fieldName, value, max, false);
        doMinValidation(fieldName, value, min, false);
    }

    private static void doSizeValidation(String fieldName, int actualSize, int minSize, int maxSize, boolean isLength) throws InvalidParameterValueException {
        if ((minSize < 0) && (maxSize < 0)) {
            return;
        } else {
            if (minSize >= 0) {
                doMinValidation(fieldName, actualSize, minSize, isLength);
            }

            if (maxSize >= 0) {
                doMaxValidation(fieldName, actualSize, maxSize, isLength);
            }

        }
    }

    private static void doValidNumberValidation(String fieldName, long value, long[] validNum) throws InvalidParameterValueException {
        boolean isValid = false;
        for (long n : validNum) {
            if (value == n) {
                isValid = true;
                break;
            }
        }

        if (isValid == false) {
            throw new InvalidParameterValueException("Field [ " + fieldName + " ] : value " + value + " is not in the valid number set.");
        }
    }

    private static void doValidStringValidation(String fieldName, String value, String[] validNum) throws InvalidParameterValueException {
        boolean isValid = false;
        for (String s : validNum) {
            if (s.equals(value)) {
                isValid = true;
                break;
            }
        }
        if (isValid == false) {
            throw new InvalidParameterValueException("Field [ " + fieldName + " ] : value " + value + " is not in the valid string set.");
        }
    }

    private static void doValidStringRegexValidation(String fieldName, String value, String patternValue, String patternName) throws InvalidParameterValueException {
        Pattern pattern = Pattern.compile(patternValue);
        Matcher matcher = pattern.matcher(value);
        if(!matcher.matches()){
            throw  new InvalidParameterValueException("Field [ " + fieldName + " ] : value " + value + " is not match the pattern " + patternName);
        }
    }

}
