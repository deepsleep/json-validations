package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/19/16.
 */

import com.jfcheng.json.parse.exception.JsonObjectParseException;
import com.jfcheng.json.parse.exception.JsonStringParseException;
import com.jfcheng.json.parse.exception.JsonValueParseException;
import com.jfcheng.json.utils.DataConversionUtils;
import com.jfcheng.json.utils.ReflectionUtils;
import com.jfcheng.json.annotation.AnnotationHelper;
import com.jfcheng.json.annotation.exception.InvalidParameterValueException;
import com.jfcheng.json.annotation.exception.RequiredFieldNotFoundException;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class JsonObject implements JsonValue {
    private static final long serialVersionUID = 5521558531152229557L;

    private Map<JsonString, JsonValue> values;

    public JsonObject() {
        this.values = new HashMap<JsonString, JsonValue>();
    }

    public JsonObject(Map<JsonString, JsonValue> values) {
        this.values = values;
    }

    public JsonObject(String jsonText) throws JsonValueParseException {
        this.values = (Map<JsonString, JsonValue>) JsonParser.parseString(jsonText).getValue();
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
            int lastIndex = strBuilder.length() - 1;
            if (strBuilder.charAt(lastIndex) == JsonControlChar.VALUE_SEPARATOR) {
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
                    if (AnnotationHelper.isEntityToJsonIgnore(f)) {
                        continue; // ignore this field
                    } else {
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
            }
            return new JsonObject(values);
        }
    }


    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (object == null || !(object instanceof JsonObject)) {
            return false;
        } else {
            Map<JsonString, JsonValue> otherValues = ((JsonObject) object).getValue();
            if (values.size() != otherValues.size()) {
                return false;
            } else {
                Set<JsonString> keys = values.keySet();
                for (JsonString k : keys) {
                    JsonValue v = values.get(k);
                    if (otherValues.containsKey(k) && v.equals(otherValues.get(k))) {
                        // k-v equals, do nothing, continues.
                    } else {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    @Override
    public String toString() {
        return toJsonText(DEFAULT_IGNORE_NULL_FIELD);
    }


    Map<Object, Object> toJavaMapValue(Field field, String fieldName, Class rawClass, Type keyType, Type valueType, boolean doValidation) throws JsonValueParseException, IllegalAccessException, InstantiationException, ClassNotFoundException, RequiredFieldNotFoundException, InvalidParameterValueException {
        if (keyType instanceof Class && (keyType == String.class || (Number.class.isAssignableFrom((Class) keyType)))) {
            Map<JsonString, JsonValue> mapValue = values;
            Map<Object, Object> map;

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
                    map.put(key, JsonParser.jsonValueToEntity(mapValue.get(j), null, String.valueOf(key), (Class) gType, types, doValidation));
                }

                if (doValidation) {
                    AnnotationHelper.doCollectionAnnotationValidation(fieldName, map, field.getAnnotations());
                }

                return map;
            } else {
                throw new JsonValueParseException("Cannot cast JSON to" + rawClass);
            }

        } else {
            throw new JsonValueParseException("JSON key is string, cannot cast to " + keyType);
        }
    }

    Object toOtherObjectValue(Field field, String fieldName, Class clazz, boolean doValidation) throws IllegalAccessException, InstantiationException, JsonValueParseException, ClassNotFoundException, RequiredFieldNotFoundException, InvalidParameterValueException {
        // Objects
        List<Field> fields = ReflectionUtils.getAllInstanceFields(clazz);
        Set<JsonString> keys = values.keySet();
        Object object = clazz.newInstance();
        for (Field f : fields) {
            Class fClass = f.getType();
            String name = AnnotationHelper.getFieldName(f);
            JsonString key = (JsonString) JsonString.toJsonValue(name);
            JsonValue jValue = values.get(key);

            if (jValue != null && jValue.getValue() != null) {
                f.setAccessible(true);
                Type t = f.getGenericType();
                if (t instanceof Class) {
                    f.set(object, JsonParser.jsonValueToEntity(jValue, f, name, fClass, null, doValidation));
                } else if (t instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) t;
                    f.set(object, JsonParser.jsonValueToEntity(jValue, f, name, fClass, pType.getActualTypeArguments(), doValidation));
                } else {
                    throw new JsonValueParseException("Cannot cast JSON to" + clazz);
                }
            } else if ((jValue == null || jValue.getValue() == null) && doValidation) {
                AnnotationHelper.doRequiredAnnotationValidation(name, jValue, f.getAnnotations());
            }

        }
        return object;
    }


    // Methods to support dynamically operations on a JsonObject


    public boolean isEmpty() {
        return values.isEmpty();
    }

    public int size() {
        return values.size();
    }

    public boolean containsKey(String key) throws JsonStringParseException {
        JsonString jKey = (JsonString) JsonString.toJsonValue(key);
        return values.containsKey(jKey);
    }

    public Set<String> keySet() {
        Set<JsonString> jKeySet = values.keySet();
        Set<String> keySet = new HashSet<String>();
        jKeySet.forEach(k -> keySet.add(k.getValue()));
        return keySet;
    }

    public Object get(String key) throws JsonStringParseException {
        JsonString jKey = (JsonString) JsonString.toJsonValue(key);

        JsonValue jValue = values.get(jKey);
        if (jValue != null) {
            return jValue.getValue();
        } else {
            return null;
        }
    }

    private JsonValue getJsonValue(String key) throws JsonStringParseException {
        JsonString jKey = (JsonString) JsonString.toJsonValue(key);
        return values.get(jKey);
    }


    public JsonObject getJsonObject(String key) throws JsonValueParseException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonObject) {
                return (JsonObject) value;
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a JsonObject.");
            }
        }
    }


    public String getString(String key) throws JsonValueParseException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonString) {
                return (String) value.getValue();
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a string");
            }
        }
    }

    public Byte getByte(String key) throws JsonValueParseException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonNumber) {
                return ((Number) value.getValue()).byteValue();
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a number");
            }
        }
    }

    public Short getShort(String key) throws JsonValueParseException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonNumber) {
                return ((Number) value.getValue()).shortValue();
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a number");
            }
        }
    }

    public Integer getInt(String key) throws JsonValueParseException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonNumber) {
                return ((Number) value.getValue()).intValue();
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a number");
            }
        }
    }

    public Long getLong(String key) throws JsonValueParseException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonNumber) {
                return ((Number) value.getValue()).longValue();
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a number");
            }
        }
    }


    public Float getFloat(String key) throws JsonValueParseException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonNumber) {
                return ((Number) value.getValue()).floatValue();
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a number");
            }
        }
    }

    public Double getDouble(String key) throws JsonValueParseException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonNumber) {
                return ((Number) value.getValue()).doubleValue();
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a number");
            }
        }
    }

    public Boolean getBoolean(String key) throws JsonValueParseException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonBoolean) {
                return ((JsonBoolean) value).getValue();
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a boolean");
            }
        }
    }

    public Collection getCollection(String key, Class collectionType, Class typeParameter) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidParameterValueException, RequiredFieldNotFoundException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonArray) {
                return ((JsonArray) value).toJavaCollectionValue(null, null, collectionType, typeParameter, false);
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a collection");
            }
        }
    }

    public Map getMap(String key, Class mapType, Class keyType, Class valueType) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidParameterValueException, RequiredFieldNotFoundException {
        JsonValue value = getJsonValue(key);
        if (value == null) {
            return null;
        } else {
            if (value instanceof JsonObject) {
                return ((JsonObject) value).toJavaMapValue(null, null, mapType, keyType, valueType, false);
            } else {
                throw new JsonValueParseException("The value of " + key + " is not a map");
            }
        }
    }

    /**
     * Put a key-value to the JsonObject if the key is not exist before.
     * If the key exist, do nothing and return false; else put the k-v into the JsonObject and return true;
     *
     * @param key
     * @param value
     * @throws JsonValueParseException
     */
    public boolean putIfAbsent(String key, Object value) throws JsonValueParseException {
        JsonString jKey = (JsonString) JsonString.toJsonValue(key);
        if (values.containsKey(jKey)) {
            return false;
        } else {
            JsonValue jValue = JsonParser.toJsonValue(value);
            values.put(jKey, jValue);
            return true;
        }
    }


    /**
     * Put a key-value to the JsonObject. If the key was exist, replace the value; else add the key-value
     * to the JsonObject.
     *
     * @param key
     * @param value
     * @throws JsonValueParseException
     */
    public void put(String key, Object value) throws JsonValueParseException {
        JsonString jKey = (JsonString) JsonString.toJsonValue(key);
        JsonValue jValue = JsonParser.toJsonValue(value);
        values.put(jKey, jValue);
    }

}
