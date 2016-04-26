package com.jfcheng.json.mapping;

import com.google.gson.JsonParseException;
import com.jfcheng.json.parse.*;
import com.jfcheng.json.parse.exception.JsonValueParseException;
import com.jfcheng.utils.ReflectionUtils;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by jfcheng on 4/25/16.
 */
public class JsonUtils {

    public static String entityToJsonText(Object obj) throws JsonValueParseException {
        JsonValue jsonValue = JsonParser.toJsonValue(obj);
        return String.valueOf(jsonValue.toJsonText());
    }

    public static Object jsonTextToEntity(Reader reader, Class clazz) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        JsonValue jsonValue = JsonParser.parse(reader);
        return jsonValueToEntity(jsonValue,clazz,null);
    }

    public static Object jsonValueToEntity(JsonValue jsonValue, Class rawClass, Type[] parameterTypes) throws JsonValueParseException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        if (jsonValue instanceof JsonNull) {
            return null;
        } else if (jsonValue instanceof JsonBoolean) {
            return toJavaBooleanValue((JsonBoolean) jsonValue, rawClass);
        } else if (jsonValue instanceof JsonNumber) {
            return toJavaNumberValue((JsonNumber) jsonValue, rawClass);
        } else if (jsonValue instanceof JsonString) {
            return toJavaStringValue((JsonString) jsonValue, rawClass);
        } else if (jsonValue instanceof JsonArray) {
            if (rawClass.isArray()) {
                return toJavaArrayValue((JsonArray) jsonValue, rawClass);
            } else if (parameterTypes != null && parameterTypes.length == 1) {
                return toJavaCollectionValue((JsonArray) jsonValue, rawClass, parameterTypes[0]);
            } else {
                throw new JsonValueParseException("Cannot cast to class " + rawClass + " with types " + parameterTypes);
            }
        } else if (jsonValue instanceof JsonObject) {
            if (Map.class.isInstance(rawClass)) {
                if (parameterTypes != null && parameterTypes.length == 2) {
                    return toJavaMapValue((JsonObject) jsonValue, rawClass, parameterTypes[0], parameterTypes[1]);
                } else {
                    throw new JsonValueParseException("Cannot cast to class " + rawClass + " with types " + parameterTypes);
                }
            } else {
                return toOtherObjectValue((JsonObject) jsonValue, rawClass);
            }
        } else {
            throw new JsonParseException("Cannot cast " + jsonValue.getValue() + " to " + rawClass);
        }

    }


    private static Object toJavaBooleanValue(JsonBoolean jsonValue, Class<?> clazz) {
        Boolean value = jsonValue.getValue();
        if (clazz == Boolean.class) {
            return value;
        } else {
            return value;
        }
    }

    private static Object toJavaNumberValue(JsonNumber jsonValue, Class<?> clazz) {
        Number num = jsonValue.getValue();
        if (clazz == Long.class || clazz == Long.TYPE) {
            return num.longValue();
        } else if (clazz == Integer.class || clazz == Integer.TYPE) {
            return num.intValue();
        } else if (clazz == Short.class || clazz == Short.TYPE) {
            return num.shortValue();
        } else if (clazz == Double.class|| clazz == Double.TYPE) {
            return num.doubleValue();
        } else if (clazz == Float.class || clazz == Float.TYPE) {
            return num.floatValue();
        } else if (clazz == Byte.class || clazz == Byte.TYPE) {
            return num.byteValue();
        } else {
            return num;
        }
    }

    private static Object toJavaStringValue(JsonString jsonValue, Class<?> clazz) {
        String strVal = jsonValue.getValue();
        if (clazz == String.class) {
            return strVal;
        } else if (clazz == Character.class && strVal.length() == 1) {
            return strVal.charAt(0);
        } else if (clazz.isEnum()) {
            Class<Enum> eClass = (Class<Enum>) clazz;
            return Enum.valueOf(eClass, strVal);
        } else {
            return strVal;
        }
    }

    private static Object toJavaArrayValue(JsonArray jsonValue, Class<?> classType) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<JsonValue> list = jsonValue.getValue();
       // List<Object> listObject = new ArrayList<>();
        Object array = Array.newInstance(classType.getComponentType(), list.size());
        //componentType =
        int i = 0;
        for (JsonValue v : list) {
           Array.set(array, i, jsonValueToEntity(v, classType.getComponentType(),null));
            i++;
        }
        return array;
    }

    private static Collection<Object> toJavaCollectionValue(JsonArray jsonValue, Class rawClass, Type genericType) throws IllegalAccessException, InstantiationException, JsonValueParseException, ClassNotFoundException {
        List<JsonValue> jsonValues = jsonValue.getValue();
        Collection<Object> listObject = null;

        if (rawClass.isInterface() || Modifier.isAbstract(rawClass.getModifiers())) {
            listObject = new ArrayList<>();
        } else {
            listObject = (Collection<Object>) rawClass.newInstance();
        }

        if (genericType instanceof Class) { // Not
            Class gType = (Class) genericType;
            for (JsonValue val : jsonValues) {
                listObject.add(jsonValueToEntity(val, gType, null));
            }
            return listObject;
        } else if (genericType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) genericType;
            Class rClass = (Class) pType.getRawType();
            Type[] types = pType.getActualTypeArguments();
            for (JsonValue val : jsonValues) {
                listObject.add(jsonValueToEntity(val, rClass, types));
            }
            return listObject;
        } else {
            throw new JsonValueParseException("Cannot cast JSON to" + rawClass);
        }
    }

    public static Map<String, Object> toJavaMapValue(JsonObject jsonValue, Class rawClass, Type keyType, Type valueType) throws JsonValueParseException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (keyType instanceof Class && ((Class) keyType) == String.class) {
            Map<JsonString, JsonValue> mapValue = jsonValue.getValue();
            Map<String, Object> map = null;

            if (rawClass.isInterface() || Modifier.isAbstract(rawClass.getModifiers())) {
                map = new HashMap<>();
            } else {
                map = (Map<String, Object>) rawClass.newInstance();
            }

            Set<JsonString> keys = mapValue.keySet();


            if (valueType instanceof Class) { // Not
                for (JsonString j : keys) {
                    String key = j.getValue();
                    Class gType = (Class) valueType;
                    map.put(key, jsonValueToEntity(mapValue.get(j), gType, null));
                }
                return map;
            } else if (valueType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) valueType;
                Class rClass = (Class) pType.getRawType();
                Type[] types = pType.getActualTypeArguments();
                for (JsonString j : keys) {
                    String key = j.getValue();
                    map.put(key, jsonValueToEntity(mapValue.get(j), rClass, types));
                }
                return map;
            } else {
                throw new JsonValueParseException("Cannot cast JSON to" + rawClass);
            }

        } else {
            throw new JsonParseException("JSON key is string, cannot cast to " + keyType);
        }
    }

    public static Object toOtherObjectValue(JsonObject value, Class clazz) throws IllegalAccessException, InstantiationException, JsonValueParseException, ClassNotFoundException {
        // Objects
        List<Field> fields = ReflectionUtils.getAllInstanceFields(clazz);
        Set<JsonString> keys = value.getValue().keySet();
        Object object = clazz.newInstance();
        for (Field f : fields) {
            Class fClass = f.getType();
            String name = f.getName();
            for (JsonString key : keys) {
                JsonValue jValue = (JsonValue) value.getValue().get(key);
                if (key.getValue().equals(name)) {
                    f.setAccessible(true);
                    Type t = f.getGenericType();
                    if(t instanceof  Class){
                        f.set(object, jsonValueToEntity(jValue,fClass,null));
                    }else if(t instanceof  ParameterizedType){
                         ParameterizedType pType = (ParameterizedType) t;
                        f.set(object,jsonValueToEntity(jValue,fClass, pType.getActualTypeArguments()));
                    }else{
                        throw new JsonValueParseException("Cannot cast JSON to" + clazz);
                    }
                    break;
                }
            }
        }
        return object;
    }

    public static void main(String[] args) throws JsonValueParseException, IllegalAccessException, InstantiationException {
        JsonValue j = JsonParser.toJsonValue(1L);
        List<String> l;

        List<Field> fields = ReflectionUtils.getAllInstanceFields(TestClass.class);
        for (Field f : fields) {
            System.out.println("??????name: " + f.getName() + " , Type " + f.getType() + " generic:  " );

            Type t = f.getGenericType();
            if(t instanceof  Class ){
                System.out.println(">>>>>>>>"+ t);
            }else if(t instanceof  ParameterizedType){
                ParameterizedType tP = (ParameterizedType) t;
                System.out.println("--" + f.getName() + ":   "+ tP.getActualTypeArguments());
            }

            System.out.println("-------------------------");
            Object[] aaa = {1,2,4};
            Object[] bbb = aaa.getClass().newInstance();

        }

    }

    class TestClass {

        List<String> l1;
        List<ArrayList<Integer>> l2;
        List<Map<String, String>> m1;
        Map<Integer, String> m2;
        Map<String, String>[] m3;


    }

}
