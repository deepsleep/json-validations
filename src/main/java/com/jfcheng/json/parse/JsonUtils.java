package com.jfcheng.json.parse;

import com.jfcheng.json.annotation.exception.ValidationFailException;
import com.jfcheng.json.parse.exception.JsonValueParseException;
import com.jfcheng.json.annotation.exception.InvalidParameterValueException;
import com.jfcheng.json.annotation.exception.RequiredFieldNotFoundException;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

/**
 * Created by jfcheng on 4/28/16.
 */
public class JsonUtils {

    public static String entityToJsonText(Object obj) throws JsonValueParseException {
        JsonValue jsonValue = JsonParser.toJsonValue(obj);
        return String.valueOf(jsonValue.toJsonText());
    }

    public static Object jsonTextToEntity(Reader reader, Class clazz) throws JsonValueParseException, ClassNotFoundException, InstantiationException {
        JsonValue jsonValue;
        Object obj = null;
        try {
            jsonValue = JsonParser.parse(reader);
            obj=  JsonParser.jsonValueToEntity(jsonValue, null, null, clazz, null,false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException| ValidationFailException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object jsonTextToEntityWithValidation(Reader reader, Class clazz) throws JsonValueParseException, ClassNotFoundException, InstantiationException,ValidationFailException{
        return jsonTextToEntityWithValidation(reader,clazz,null);
    }

    public static Object jsonTextToEntityWithValidation(Reader reader, Class clazz,Type[] parameterTypes) throws JsonValueParseException, ClassNotFoundException, InstantiationException,ValidationFailException{
        JsonValue jsonValue;
        Object obj = null;
        try {
            jsonValue = JsonParser.parse(reader);
            obj = JsonParser.jsonValueToEntity(jsonValue, null, null, clazz, parameterTypes,true);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public static Object jsonStringToEntity(String string, Class clazz) throws JsonValueParseException, ClassNotFoundException, InstantiationException {
      return jsonStringToEntity(string,clazz, null);
    }

    public static Object jsonStringToEntity(String string, Class clazz, Type[] parameterTypes) throws JsonValueParseException, ClassNotFoundException, InstantiationException {
        JsonValue jsonValue = JsonParser.parseString(string);
        Object obj = null;
        try {
           obj = JsonParser.jsonValueToEntity(jsonValue,null, null, clazz, parameterTypes,false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvalidParameterValueException e) {
            e.printStackTrace();
        } catch (RequiredFieldNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object jsonStringToEntityWithValidation(String string, Class clazz) throws JsonValueParseException, ClassNotFoundException, InstantiationException,ValidationFailException{
       return jsonStringToEntityWithValidation(string,clazz,null);
    }

    public static Object jsonStringToEntityWithValidation(String string, Class clazz, Type[] parameterTypes) throws JsonValueParseException, ClassNotFoundException, InstantiationException,ValidationFailException{
        JsonValue jsonValue = JsonParser.parseString(string);
        Object obj = null;
        try {
            obj=  JsonParser.jsonValueToEntity(jsonValue,null, null, clazz, parameterTypes,true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }






    //TODO:
    public static Object jsonStringToArray(){
        return null;
    }
}
