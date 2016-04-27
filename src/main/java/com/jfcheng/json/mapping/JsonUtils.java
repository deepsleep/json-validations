package com.jfcheng.json.mapping;

import com.google.gson.JsonParseException;
import com.jfcheng.json.parse.*;
import com.jfcheng.json.parse.exception.JsonValueParseException;
import com.jfcheng.utils.DataConversionUtils;
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
        return jsonValueToEntity(jsonValue, null, clazz, null);
    }

    public static Object jsonStringToEntity(String string, Class clazz) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        JsonValue jsonValue = JsonParser.parseString(string);
        return jsonValueToEntity(jsonValue, null, clazz, null);
    }

    public static Object jsonStringToEntity(String string, Class clazz, Type[] parameterTypes) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        JsonValue jsonValue = JsonParser.parseString(string);
        return jsonValueToEntity(jsonValue, null, clazz, parameterTypes);
    }

    private static Object jsonValueToEntity(JsonValue jsonValue, Field field, Class rawClass, Type[] parameterTypes) throws JsonValueParseException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (jsonValue instanceof JsonNull) {
            return null;
        } else if (jsonValue instanceof JsonBoolean) {
            return toJavaBooleanValue((JsonBoolean) jsonValue, field, rawClass);
        } else if (jsonValue instanceof JsonNumber) {
            return toJavaNumberValue((JsonNumber) jsonValue, field, rawClass);
        } else if (jsonValue instanceof JsonString) {
            return toJavaStringValue((JsonString) jsonValue, field, rawClass);
        } else if (jsonValue instanceof JsonArray) {
            if (rawClass.isArray()) {
                return toJavaArrayValue((JsonArray) jsonValue, field, rawClass);
            } else if (Collection.class.isAssignableFrom(rawClass) && parameterTypes != null && parameterTypes.length == 1) {
                return toJavaCollectionValue((JsonArray) jsonValue, field, rawClass, parameterTypes[0]);
            } else {
                throw new JsonValueParseException("Cannot cast to class " + rawClass + " with types " + parameterTypes);
            }
        } else if (jsonValue instanceof JsonObject) {

            if (Map.class.isAssignableFrom(rawClass)) {
                if (parameterTypes != null && parameterTypes.length == 2) {
                    return toJavaMapValue((JsonObject) jsonValue, field, rawClass, parameterTypes[0], parameterTypes[1]);
                } else {
                    throw new JsonValueParseException("Cannot cast to class " + rawClass + " with types " + parameterTypes);
                }
            } else {
                return toOtherObjectValue((JsonObject) jsonValue, field, rawClass);
            }
        } else {
            throw new JsonParseException("Cannot cast " + jsonValue.getValue() + " to " + rawClass);
        }

    }


    private static Object toJavaBooleanValue(JsonBoolean jsonValue, Field field, Class<?> clazz) {
        Boolean value = jsonValue.getValue();
        if (clazz == Boolean.class) {
            return value;
        } else {
            return value;
        }
    }

    private static Object toJavaNumberValue(JsonNumber jsonValue, Field field, Class<?> clazz) {
        Number num = jsonValue.getValue();
        return DataConversionUtils.numberToPrimitive(num, clazz);
    }

    private static Object toJavaStringValue(JsonString jsonValue, Field field, Class<?> clazz) {
        String strVal = jsonValue.getValue();
        if (clazz == String.class) {
            return strVal;
        } else if ((clazz == Character.class || clazz == Character.TYPE) && strVal.length() == 1) {
            return strVal.charAt(0);
        } else if (clazz.isEnum()) {
            Class<Enum> eClass = (Class<Enum>) clazz;
            return Enum.valueOf(eClass, strVal);
        } else {
            return strVal;
        }
    }

    private static Object toJavaArrayValue(JsonArray jsonValue, Field field, Class<?> classType) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<JsonValue> list = jsonValue.getValue();
        // List<Object> listObject = new ArrayList<>();

        Class componentType = classType.getComponentType();
        Object array = Array.newInstance(componentType, list.size());

        Type[] gTypes = null;
        if (field != null) {
            Type type = field.getGenericType();
            if (type instanceof GenericArrayType) {
                GenericArrayType gType = (GenericArrayType) type;
                ParameterizedType pType = (ParameterizedType) gType.getGenericComponentType();
                componentType = (Class) pType.getRawType();
                gTypes = pType.getActualTypeArguments();

            }
        }
        int i = 0;
        for (JsonValue v : list) {
            Array.set(array, i, jsonValueToEntity(v, null, componentType, gTypes));
            i++;
        }
        return array;
    }

    private static Collection<Object> toJavaCollectionValue(JsonArray jsonValue, Field field, Class rawClass, Type genericType) throws IllegalAccessException, InstantiationException, JsonValueParseException, ClassNotFoundException {
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
                listObject.add(jsonValueToEntity(val, null, gType, null));
            }
            return listObject;
        } else if (genericType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) genericType;
            Class rClass = (Class) pType.getRawType();
            Type[] types = pType.getActualTypeArguments();
            for (JsonValue val : jsonValues) {
                listObject.add(jsonValueToEntity(val, null, rClass, types));
            }
            return listObject;
        } else {
            throw new JsonValueParseException("Cannot cast JSON to" + rawClass);
        }
    }

    public static Map<Object, Object> toJavaMapValue(JsonObject jsonValue, Field field, Class rawClass, Type keyType, Type valueType) throws JsonValueParseException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (keyType instanceof Class && (keyType == String.class || (Number.class.isAssignableFrom((Class) keyType)))) {
            Map<JsonString, JsonValue> mapValue = jsonValue.getValue();
            Map<Object, Object> map = null;

            if (rawClass.isInterface() || Modifier.isAbstract(rawClass.getModifiers())) {
                map = new HashMap<>();
            } else {
                map = (Map<Object, Object>) rawClass.newInstance();
            }

            Set<JsonString> keys = mapValue.keySet();

            if (valueType instanceof Class || valueType instanceof ParameterizedType) { // Not
                Type gType = valueType;

//                for (JsonString j : keys) {
//                    String key = j.getValue();
//                    Class gType = (Class) valueType;
//                    map.put(key, jsonValueToEntity(mapValue.get(j),null, gType, null));
//                }
//                return map;
                Type[] types = null;
                if (valueType instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) valueType;
                    gType = pType.getRawType();
                    types = pType.getActualTypeArguments();
                }

                for (JsonString j : keys) {
                    Object key = j.getValue();
                    if(Number.class.isAssignableFrom((Class<?>) keyType)){
                        key = DataConversionUtils.stringToNumber(j.getValue() ,(Class)keyType);
                    }
                    map.put(key, jsonValueToEntity(mapValue.get(j), null, (Class) gType, types));
                }
                return map;
            } else {
                throw new JsonValueParseException("Cannot cast JSON to" + rawClass);
            }

        } else {
            throw new JsonParseException("JSON key is string, cannot cast to " + keyType);
        }
    }

    public static Object toOtherObjectValue(JsonObject value, Field field, Class clazz) throws IllegalAccessException, InstantiationException, JsonValueParseException, ClassNotFoundException {
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
                    if (t instanceof Class) {
                        f.set(object, jsonValueToEntity(jValue, f, fClass, null));
                    } else if (t instanceof ParameterizedType) {
                        ParameterizedType pType = (ParameterizedType) t;
                        f.set(object, jsonValueToEntity(jValue, f, fClass, pType.getActualTypeArguments()));
                    } else {
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
            // System.out.println("??????name: " + f.getName() + " , Type " + f.getType() + " generic:  " );

            Class c = f.getClass();

            Type t = f.getGenericType();
            if (t instanceof GenericArrayType) {
                GenericArrayType gType = (GenericArrayType) t;
                ParameterizedType pType = (ParameterizedType) gType.getGenericComponentType();
                Class rType
                        = (Class) pType.getRawType();
                Type[] gTypes = pType.getActualTypeArguments();
                System.out.println("class: " + c);
                System.out.println("generic type: " + t);
                System.out.println("rawType: " + rType);
                System.out.println("gType[0]: " + gTypes[0]);
            }

            Map<String, String> m = new HashMap<>();

            System.out.println(m.getClass().asSubclass(Map.class));

//            Type t = f.getGenericType();
//            if(t instanceof  Class ){
//                System.out.println(">>>>>>>>"+ t);
//            }
//
//            if(t instanceof  ParameterizedType){
//                ParameterizedType tP = (ParameterizedType) t;
//                System.out.println("--" + f.getName() + ":   "+ tP.getActualTypeArguments());
//            }
//
//            System.out.println("-------------------------");

        }

    }

    class TestClass {

        Map<String, String>[] map;
//        List<String> l1;
//        List<ArrayList<Integer>> l2;
//        List<Map<String, String>> m1;
//        Map<Integer, String> m2;
//        Map<String, String>[] m3;


    }

}
