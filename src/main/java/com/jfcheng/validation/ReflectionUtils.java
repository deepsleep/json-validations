package com.jfcheng.validation;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.*;

/**
 * Created by jfcheng on 2/27/16.
 */
public class ReflectionUtils {

    public static List<Field> getAllInstanceFields(Class clazz){
        return getAllInstanceFieldsRecursive(clazz, new ArrayList<Field>());
    }

    private static List<Field> getAllInstanceFieldsRecursive(Class clazz, List<Field> fields){
        Class superClass = clazz.getSuperclass();
        if(superClass != null){
            getAllInstanceFieldsRecursive(superClass,fields);
        }
        Field[] classFields = clazz.getDeclaredFields();
        for(Field f: classFields){
            if(isInstanceField(f)){
                fields.add(f);
            }

        }

        return fields;
    }

    private static boolean isInstanceField(Field f){
        //validate modifier
        int m = f.getModifiers();
        if(Modifier.isStatic(m)||Modifier.isNative(m)){
            return false;
        }else{
            return true;
        }
    }


}
