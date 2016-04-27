package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseException;

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

    public static Object jsonTextToEntity(Reader reader, Class clazz) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        JsonValue jsonValue = JsonParser.parse(reader);
        return JsonParser.jsonValueToEntity(jsonValue, null, clazz, null);
    }

    public static Object jsonStringToEntity(String string, Class clazz) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        JsonValue jsonValue = JsonParser.parseString(string);
        return JsonParser.jsonValueToEntity(jsonValue, null, clazz, null);
    }

    public static Object jsonStringToEntity(String string, Class clazz, Type[] parameterTypes) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        JsonValue jsonValue = JsonParser.parseString(string);
        return JsonParser.jsonValueToEntity(jsonValue, null, clazz, parameterTypes);
    }

    //TODO:
    public static Object jsonStringToArray(){
        return null;
    }
}
