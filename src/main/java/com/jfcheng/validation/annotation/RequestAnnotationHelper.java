package com.jfcheng.validation.annotation;

import com.jfcheng.validation.exception.InvalidParameterValueException;
import com.jfcheng.validation.exception.RequiredFieldNotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Created by jfcheng on 2/28/16.
 */
public class RequestAnnotationHelper {

    private RequestAnnotationHelper(){}

    public static void doRequiredAnnotationValidation(Object value, Annotation[] annotations) throws RequiredFieldNotFoundException {
        if(value == null && annotations != null){
            for(Annotation a: annotations){
                if(a instanceof Required){
                    if(((Required) a).value() == true){
                    throw new RequiredFieldNotFoundException("");
                    }
                }
            }

        }
    }

    public static void doNumberAnnotationValidation(Object value, Annotation[] annotations) throws InvalidParameterValueException {
        if(annotations !=null){
            Number numberValue = (Number) value;
            long num = numberValue.longValue();
            for(Annotation a: annotations){
                if(a instanceof MinValue){
                    doMinValidation(num, ((MinValue) a).value());
                }

                if(a instanceof MaxValue){
                    doMaxValidation(num, ((MaxValue) a).value());
                }

                if(a instanceof RangeValue){
                    doRangeValueValidation(num, ((RangeValue) a).min(),((RangeValue) a).max());
                }

                if(a instanceof ValidNumbers){
                    doValidNumberValidation(num, ((ValidNumbers) a).value());
                }
            }

        }
    }

    public static void doStringAnnotationValidation(Object value, Annotation[] annotations) throws InvalidParameterValueException {
        if(annotations !=null){
            String str = (String) value;
            int size = str.length();
            for(Annotation a: annotations){
                if(a instanceof ValidLength){
                    doSizeValidation(size, ((ValidLength) a).min(),((ValidLength) a).max());
                }

                if(a instanceof  ValidStrings){
                    doValidStringValidation(str, ((ValidStrings) a).value());
                }
            }
        }
    }

    public static void doListAnnotationValidation(Object value, Annotation[] annotations) throws InvalidParameterValueException {
        if(annotations != null) {
            int size = -1;
            if (value.getClass().isArray()) {
                size =  Array.getLength(value);
            } else if (value instanceof Collection) {
                size = ((Collection) value).size();
            }

            for (Annotation a : annotations) {
                if (a instanceof ValidLength) {
                    doSizeValidation(size, ((ValidLength) a).min(), ((ValidLength) a).max());
                }
            }
        }
    }

    public static boolean isRequestParameterIgnore(Object value, Annotation[] annotations){
        boolean isIgnore = false;
        if(annotations != null){
            for(Annotation a: annotations){
                if(a instanceof JsonIgnore){
                    isIgnore = ((JsonIgnore) a).request();
                    break;
                }
            }
        }
        return isIgnore;
    }

    public static boolean isResponseParameterIgnore(Object value, Annotation[] annotations){
        boolean isIgnore = false;
        if(annotations != null){
            for(Annotation a: annotations){
                if(a instanceof JsonIgnore){
                    isIgnore = ((JsonIgnore) a).response();
                    break;
                }
            }
        }
        return isIgnore;
    }


    private static void doMinValidation(long value, long min) throws InvalidParameterValueException {
        if(value < min){
            throw new InvalidParameterValueException(value + " is less than min value " + min );
        }
    }

    private static void doMaxValidation(long value,  long max) throws InvalidParameterValueException {
        if(value > max){
            throw new InvalidParameterValueException(value + " is greater than max value " + max );
        }
    }

    private static void doRangeValueValidation(long value, long min, long max) throws InvalidParameterValueException {
        doMaxValidation(value, max);
        doMinValidation(value, min);
    }

    private static void doSizeValidation(int actualSize, int minSize, int maxSize) throws InvalidParameterValueException {
        if((minSize < 0) && (maxSize < 0)){
            return;
        }else{
            if(minSize  >= 0 ){
                doMinValidation(actualSize, minSize);
            }

            if(maxSize >= 0){
                doMaxValidation(actualSize, maxSize);
            }

        }
    }

    private static void doValidNumberValidation(long value, long[] validNum) throws InvalidParameterValueException {
        boolean isValid = false;
        for(long n: validNum){
            if(value == n){
                isValid = true;
                break;
            }
        }

        if(isValid == false){
            throw new InvalidParameterValueException(value + " is not in the valid number set.");
        }
    }

    private static void doValidStringValidation(String value, String[] validNum) throws InvalidParameterValueException {
        boolean isValid = false;
        for(String s: validNum){
            if(s.equals(value)){
                isValid = true;
                break;
            }
        }
        if(isValid == false){
            throw new InvalidParameterValueException(value + " is not in the valid string set.");
        }
    }



}
