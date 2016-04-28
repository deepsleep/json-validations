package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/19/16.
 */

import com.google.gson.JsonParseException;
import com.jfcheng.json.parse.exception.JsonObjectParseException;
import com.jfcheng.json.parse.exception.JsonValueParseException;
import com.jfcheng.utils.DataConversionUtils;
import com.jfcheng.utils.ReflectionUtils;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonObject implements JsonValue {
    private static final long serialVersionUID = 5521558531152229557L;

    private Map<JsonString, JsonValue> values;

    public JsonObject() {
        this.values = new HashMap<JsonString, JsonValue>();
    }

    public JsonObject(Map<JsonString, JsonValue> values) {
        this.values = values;
    }

    static JsonObject parse(Reader reader) throws IOException, JsonValueParseException {

        Map<JsonString, JsonValue> values = new HashMap<JsonString, JsonValue>();
        int val = reader.read();
        boolean isObjectEndWasFound = false;
        while (val != -1) {
            char c = (char) val;

            if (JsonControlChar.isWhitespaceChar(c)) {
                val = reader.read(); // skip all the whitespace('','\t','\n','r'). continue
            } else if (JsonControlChar.isObjectEnd(c)) {
                isObjectEndWasFound = true;
                break;
            } else if (JsonControlChar.isValueSeparator(c)) {
                if (values.size() == 0) {
                    throw new JsonObjectParseException("Paring object fail: invalid object structure '{,'");  // { , exception.
                } else {
                    int tmpVal = addKVPair(values, reader, JsonControlChar.MEANINGLESS_CHAR); // look for a k-v
                    if (tmpVal != JsonControlChar.MEANINGLESS_CHAR) {
                        val = tmpVal;
                    } else {
                        val = reader.read();
                    }
                }
            } else if (values.size() == 0) {
                int tmpVal = addKVPair(values, reader, val);
                if (tmpVal != JsonControlChar.MEANINGLESS_CHAR) {
                    val = tmpVal;
                } else {
                    val = reader.read();
                }
            } else {
                throw new JsonObjectParseException("Invalid json object structure: invalid char was got: " + c);
            }
        }

        if (isObjectEndWasFound) {
            return new JsonObject(values);
        } else {
            throw new JsonObjectParseException("Json object parsing error: Unclosed object. Expecting ',' or ']',  but got 'EOF' ");
        }

    }

    private static int addKVPair(Map<JsonString, JsonValue> values, Reader reader, int lastReadChar) throws IOException, JsonValueParseException {
        JsonParserResult returnValue = JsonKVPair.parse(reader, lastReadChar);
        JsonKVPair kvPair = (JsonKVPair) returnValue.getValue();
        // check if the key is duplicated
        if (values.containsKey(kvPair.getName())) {
            throw new JsonObjectParseException("Duplicated key in the json object: " + kvPair.getName());
        } else {
            values.put(kvPair.getName(), kvPair.getValue());
            return returnValue.getLastCharRead();
        }
    }


    @Override
    public Map getValue() {
        return values;
    }

    @Override
    public String toJsonText(boolean ignoreNullField) {
        if (values == null) {
            return "null";
        } else {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(JsonControlChar.OBJECT_BEGIN);
            Set<JsonString> keys = values.keySet();

            //int counter = 1;
            for (JsonString key : keys) {
                String k = key.toJsonText(ignoreNullField);
                Object v = values.get(key).toJsonText(ignoreNullField);

                if (!"null".equals(v) || !ignoreNullField) {
                    strBuilder.append(k);
                    strBuilder.append(JsonControlChar.NAME_SEPARATOR);
                    strBuilder.append(v);
                    //if (counter < values.size()) {
                    strBuilder.append(JsonControlChar.VALUE_SEPARATOR);
                    //}
                }
                //counter += 1;
            }
            int lastIndex = strBuilder.length()-1;
            if(strBuilder.charAt(lastIndex) == JsonControlChar.VALUE_SEPARATOR){
               strBuilder.deleteCharAt(lastIndex);
            }

            strBuilder.append(JsonControlChar.OBJECT_END);
            return strBuilder.toString();
        }
    }

    public static JsonValue toJsonValue(Object obj) throws JsonValueParseException {
        if (obj == null) {
            return new JsonNull();
        } else if (obj instanceof JsonObject) {
            return (JsonObject) obj;
        } else if (obj instanceof Map) {
            Map<JsonString, JsonValue> values = new HashMap<>();
            Map<?, ?> mapValues = (Map<?, ?>) obj;
            Set keys = ((Map) obj).keySet();
            for (Object key : keys) {
                //TODO: Decide which method to deal with the key of Map is not String.
//                if(!(key instanceof  String) && !(key instanceof  JsonString)){
//                    throw new JsonValueParseException("JsonValue only supports Maps whose keys are String. Type of " + key.getClass() + " is not supported.");
//                }

                JsonString strKey = (JsonString) JsonString.toJsonValue(key.toString()); // Get the map key.toString as the key.
                JsonValue value = JsonParser.toJsonValue(mapValues.get(key));
                values.put(strKey, value);
            }
            return new JsonObject(values);
        } else {
            // Other objects
            List<Field> fields = ReflectionUtils.getAllInstanceFields(obj.getClass());

            Map<JsonString, JsonValue> values = new HashMap<>();
            if (fields != null && fields.size() > 0) {

                for (Field f : fields) {
                    f.setAccessible(true);
                    Object fValue = null;
                    try {
                        fValue = f.get(obj);
                    } catch (IllegalAccessException e) {
                        new JsonObjectParseException(e.getMessage());
                    }
                    String fieldName = f.getName();
                    JsonString jsonStr = (JsonString) JsonString.toJsonValue(fieldName);
                    JsonValue jsonValue = JsonParser.toJsonValue(fValue);
                    values.put(jsonStr, jsonValue);

                }
            }
            return new JsonObject(values);
        }
    }


//    @Override
//    public int hashCode() {
//        return values.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        if (object == this) {
//            return true;
//        } else if (object == null || !(object instanceof JsonObject)) {
//            return false;
//        } else {
//            return values.equals(object);
//        }
//    }

    @Override
    public String toString() {
        return toJsonText(DEFAULT_IGNORE_NULL_FIELD);
    }


    Map<Object, Object> toJavaMapValue(Field field, Class rawClass, Type keyType, Type valueType) throws JsonValueParseException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (keyType instanceof Class && (keyType == String.class || (Number.class.isAssignableFrom((Class) keyType)))) {
            Map<JsonString, JsonValue> mapValue = values;
            Map<Object, Object> map = null;

            if (rawClass.isInterface() || Modifier.isAbstract(rawClass.getModifiers())) {
                map = new HashMap<>();
            } else {
                map = (Map<Object, Object>) rawClass.newInstance();
            }

            Set<JsonString> keys = mapValue.keySet();

            if (valueType instanceof Class || valueType instanceof ParameterizedType) { // Not
                Type gType = valueType;

                Type[] types = null;
                if (valueType instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) valueType;
                    gType = pType.getRawType();
                    types = pType.getActualTypeArguments();
                }

                for (JsonString j : keys) {
                    Object key = j.getValue();
                    if (Number.class.isAssignableFrom((Class<?>) keyType)) {
                        key = DataConversionUtils.stringToNumber(j.getValue(), (Class) keyType);
                    }
                    map.put(key, JsonParser.jsonValueToEntity(mapValue.get(j), null, (Class) gType, types));
                }
                return map;
            } else {
                throw new JsonValueParseException("Cannot cast JSON to" + rawClass);
            }

        } else {
            throw new JsonParseException("JSON key is string, cannot cast to " + keyType);
        }
    }

    Object toOtherObjectValue(Field field, Class clazz) throws IllegalAccessException, InstantiationException, JsonValueParseException, ClassNotFoundException {
        // Objects
        List<Field> fields = ReflectionUtils.getAllInstanceFields(clazz);
        Set<JsonString> keys = values.keySet();
        Object object = clazz.newInstance();
        for (Field f : fields) {
            Class fClass = f.getType();
            String name = f.getName();
            for (JsonString key : keys) {
                JsonValue jValue = values.get(key);
                if (key.getValue().equals(name)) {
                    f.setAccessible(true);
                    Type t = f.getGenericType();
                    if (t instanceof Class) {
                        f.set(object, JsonParser.jsonValueToEntity(jValue, f, fClass, null));
                    } else if (t instanceof ParameterizedType) {
                        ParameterizedType pType = (ParameterizedType) t;
                        f.set(object, JsonParser.jsonValueToEntity(jValue, f, fClass, pType.getActualTypeArguments()));
                    } else {
                        throw new JsonValueParseException("Cannot cast JSON to" + clazz);
                    }
                    break;
                }
            }
        }
        return object;
    }


}
