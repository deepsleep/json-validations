package com.jfcheng.validation;

import com.jfcheng.validation.annotation.JsonName;
import com.jfcheng.validation.annotation.RequestAnnotationHelper;
import com.jfcheng.validation.exception.ValidationFailException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by jfcheng on 2/27/16.
 */
public class ObjectToJsonStringValidator {

    private static ObjectToJsonStringValidator validator = new ObjectToJsonStringValidator();

    private ObjectToJsonStringValidator(){

    }

    public static ObjectToJsonStringValidator getValidator(){
        return validator;
    }


    public static Object objectToJsonStringValidation(Object value) throws ValidationFailException {
        return validator.doValueValidation(value,null, false);
    }


    public Object doValueValidation(Object value, Field field, boolean isKey) throws ValidationFailException {

        if(value == null){
            return doNullValidation(value,field,isKey);
        }else{
            String keyName =null;
            if(value!=null && field != null){
                keyName = "\"" + getFieldName(field) +"\":";
            }

            Object result;
            if(value instanceof Number){
                result =  doNumberValidation(value,field,isKey);
            }else if(value instanceof Boolean){
                result = doBooleanValidation(value,field,isKey);
            }else if(value instanceof  Character){
                result = doCharacterValidation(value,field,isKey);
            }else if(value instanceof String){
                result =  doStringValidation(value,field,isKey);
            }else if(value.getClass().isArray()){
                result =  doArrayValidation( value,field,isKey);
            }else if(value instanceof List){
                result =  doListValidation(value,field,isKey);
            }else if(value instanceof Set){
                result =  doSetValidation(value,field,isKey);
            }else if(value instanceof Map){
                result =  doMapValidation(value,field,isKey);
            }else if(value instanceof Enum){
                result =  doEnumValidation(value,field,isKey);
            }else{
                result =  doOtherObjectValidation(value,field,isKey);
            }

            if(keyName !=null){
                return keyName + result;
            }else{
                return result;
            }
        }
    }

    protected  String getFieldName(Field f){
        JsonName jsonName = f.getAnnotation(JsonName.class);
        if(jsonName != null && jsonName.value() != null){
            return jsonName.value();
        }else{
            return f.getName();
        }

    }

   
    protected Object doNullValidation(Object value, Field field, boolean isKey) throws ValidationFailException {
        return null;
    }

   
    protected Object doBooleanValidation(Object value, Field field, boolean isKey) {
        if(isKey == true){
            return "\"" + value + "\":";
        }else{
             return value;
        }
    }

    protected  Object doNumberValidation(Object value, Field field, boolean isKey){
        if(isKey == true){
            return "\"" + value + "\":";
        }else{
            return value;
        }
    }

   
    protected Object doCharacterValidation(Object value, Field field, boolean isKey) {
        if(isKey == true){
            return "\"" + value + "\":";
        }else{
            return  "\"" + value + "\"" ;
        }
    }

    protected  Object doStringValidation(Object value, Field field, boolean isKey){

        if(isKey == true){
            return "\"" + value + "\":";
        }else{
            return  "\"" + value + "\"" ;
        }
    }

    protected  String doEnumValidation(Object value, Field field, boolean isKey){
        Enum enumVal = (Enum) value;


        if(isKey == true){
            return "\"" + enumVal + "\":";
        }else{
           return  "\""+ enumVal + "\"";
        }

    }

    protected  Object doArrayValidation(Object value, Field field, boolean isKey) throws ValidationFailException {
        int length = Array.getLength(value);
        String s;
        if(isKey == true){
            s = "\"[";
        }else{
            s = "[";
        }

        String data = "";
        for(int i=0; i< length; i++){
            Object elementVal = Array.get(value,i);
            data = data + doValueValidation(elementVal,null,false);
            data = data + ",";
        }
        data = data.substring(0,data.lastIndexOf(","));

        s = s + data + "]";
        if(isKey){
            s = s + "\"";
        }
        return s;

    }

    protected  Object doListValidation(Object value, Field field, boolean isKey)  throws ValidationFailException {

        String s;
        if(isKey == true){
            s = "\"[";
        }else{
            s = "[";
        }

        String data = "";
        Iterator iterator = ((Collection) value).iterator();
        while(iterator.hasNext()){
            data = data + doValueValidation(iterator.next(),null,false);
            data = data + ",";
        }
        data = data.substring(0,data.lastIndexOf(","));

        s = s + data + "]";
        if(isKey){
            s = s + "\"";
        }
        return s;
    }

    protected  Object doSetValidation(Object value, Field field, boolean isKey) throws ValidationFailException {
        String s;
        if(isKey == true){
            s = "\"[";
        }else{
            s = "[";
        }

        String data = "";
        // System.out.println(value.getClass().getSimpleName() + " " + f.getName()  + "=" + value);
        Iterator iterator = ((Collection) value).iterator();
        while(iterator.hasNext()){
            data = data + doValueValidation(iterator.next(),null,false);
            data = data + ",";
        }
        data = data.substring(0,data.lastIndexOf(","));

        s = s + data + "]";
        if(isKey){
            s = s + "\"";
        }
        return s;
    }

    protected  Object doMapValidation(Object value, Field field, boolean isKey) throws ValidationFailException {
        StringBuilder sBuilder = new StringBuilder();

        String s;
        if(isKey == true){
            sBuilder.append("\"{");
        }else{
            sBuilder.append("{");
        }

        //System.out.println(value.getClass().getSimpleName() + " " + f.getName()  + "=" + value);
        Set keys = ((Map) value).keySet();
        String  data ="";
        if(keys!=null && keys.size() > 0) {
            for (Object key : keys) {

                sBuilder.append(doValueValidation(key, null,true));

                sBuilder.append(doValueValidation(((Map) value).get(key), null,false));
                sBuilder.append(",");
            }
             data = sBuilder.substring(0, sBuilder.lastIndexOf(","));
        }else{
            data = sBuilder.toString();
        }

        data = data + "}";
        if(isKey == true){
            data = data + "\"";
        }

        return data;

    }

    protected Object doOtherObjectValidation(Object value, Field field, boolean isKey) throws ValidationFailException {

        return objectToJsonString(value,field,isKey);
    }


    private  <T> String objectToJsonString(T object, Field field, boolean isKey) throws ValidationFailException  {
        if(object.getClass() == null){
            return null;
        }else {
            Class<T> clazz = (Class<T>) object.getClass();
            // T newObject = clazz.newInstance();
            List<Field> fields = ReflectionUtils.getAllInstanceFields(clazz);

            boolean isArray = false;
            StringBuilder sbuilder = new StringBuilder();
            if (clazz.isArray() || object instanceof List || object instanceof Set) {
                isArray = true;
                sbuilder.append("[");
            } else {
                sbuilder.append("{");
            }

            String json = sbuilder.toString();
            if (fields != null && fields.size() > 0) {

                for (Field f : fields) {
                    if(RequestAnnotationHelper.isRequestParameterIgnore(null, f.getAnnotations()) == false) {
                        f.setAccessible(true);
                        Object value = null;
                        try {
                            value = f.get(object);
                        } catch (IllegalAccessException e) {
                            new ValidationFailException(e.getMessage(), e);
                        }
                        String fieldName = getFieldName(f);
                        if (value != null) {
                            sbuilder.append("\"" + fieldName + "\":");
                            sbuilder.append(doValueValidation(value, null,false));
                            sbuilder.append(",");
                        }
                    }
                }

                json = sbuilder.substring(0, sbuilder.lastIndexOf(","));
            }
            if(isArray == true){
                json += "]";
            }else{
                json += "}";
            }
            return json;
        }
    }
}
