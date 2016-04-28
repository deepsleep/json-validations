package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonArrayParseException;
import com.jfcheng.json.parse.exception.JsonValueParseException;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by jfcheng on 4/19/16.
 */
public class JsonArray implements JsonValue {
    private static final long serialVersionUID = -3627576530393535452L;

    private ArrayList<JsonValue> values;

    public JsonArray() {
        values = new ArrayList<JsonValue>();
    }

    public JsonArray(ArrayList<JsonValue> values) {
        this.values = values;
    }


    static JsonArray parse(Reader reader) throws IOException, JsonValueParseException {
        ArrayList<JsonValue> arrayValues = new ArrayList<>();
        boolean preCharIsValueSeparator = false;
        boolean foundTheArrayEnd = false;
        int val = reader.read();

        while (val != JsonControlChar.END_OF_READ) {
            char c = (char) val;

            if (JsonControlChar.isWhitespaceChar(c)) {
                // skip all the whitespaces
                val = reader.read();
            } else if (JsonControlChar.isArrayEnd(c)) {
                if (preCharIsValueSeparator) {
                    throw new JsonArrayParseException("Json array parsing error: ',' expecting a value, but got '" + c + "'");
                } else {
                    foundTheArrayEnd = true;
                    break; // end of the array parsing.
                }
            } else if (JsonControlChar.isValueSeparator(c)) {
                if (arrayValues.size() > 0) {
                    preCharIsValueSeparator = true;  // found value separator, continue
                    val = reader.read();
                } else {
                    throw new JsonArrayParseException("Json array parsing error: '[' expecting a json value or ']' but got '" + c + "'");
                }
            } else if (arrayValues.size() == 0 || (preCharIsValueSeparator == true && arrayValues.size() > 0)) {
                JsonParserResult returnValue = JsonParser.parse(reader, val);
                arrayValues.add(returnValue.getValue());
                if (returnValue.getLastCharRead() != JsonControlChar.MEANINGLESS_CHAR) {
                    val = returnValue.getLastCharRead();
                } else {
                    val = reader.read();
                }
                preCharIsValueSeparator = false;
            } else {
                throw new JsonArrayParseException("Json array parsing error: unexpected char: " + c);
            }
        }
        if (foundTheArrayEnd) {
            return new JsonArray(arrayValues);
        } else {
            throw new JsonArrayParseException("Json array parsing error: Unclosed array. Expecting ',' or ']',  but got 'EOF' ");
        }
    }

    @Override
    public List<JsonValue> getValue() {
        return values;
    }

    @Override
    public String toJsonText(boolean ignoreNullField) {
        if (values == null) {
            return "null";
        } else {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(JsonControlChar.ARRAY_BEGIN);

            int i = 0;
            while (i < values.size() - 1) {
                strBuilder.append(values.get(i).toJsonText(ignoreNullField));
                strBuilder.append(JsonControlChar.VALUE_SEPARATOR);
                i += 1;
            }

            // Add the last one elements
            if (values.size() > 0) {
                strBuilder.append(values.get(i).toJsonText(ignoreNullField));
            }

            strBuilder.append(JsonControlChar.ARRAY_END);
            return strBuilder.toString();
        }
    }

    public static JsonValue toJsonValue(Object obj) throws JsonValueParseException {
        if (obj == null) {
            return new JsonNull();
        } else if (obj instanceof JsonArray) {
            return (JsonArray) obj;
        } else if (obj instanceof List || obj instanceof Set) {
            ArrayList<JsonValue> values = new ArrayList<JsonValue>();
            Iterator iter = ((Collection) obj).iterator();
            while (iter.hasNext()) {
                values.add(JsonParser.toJsonValue(iter.next()));
            }
            return new JsonArray(values);

        } else if (obj.getClass().isArray()) {
            ArrayList<JsonValue> values = new ArrayList<JsonValue>();

            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object elementVal = Array.get(obj, i);
                values.add(JsonParser.toJsonValue(elementVal));
            }

            return new JsonArray(values);
        } else {
            throw new JsonArrayParseException("Cannot cast " + obj.getClass() + " to JsonArray");
        }
    }


//    @Override
//    public int hashCode() {
//        return values.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == this) {
//            return true;
//        } else if (o == null || !(o instanceof JsonArray) ) {
//            return false;
//        } else {
//            return values.equals(o);
//        }
//    }

    @Override
    public String toString() {
        return toJsonText(DEFAULT_IGNORE_NULL_FIELD);
    }


    Object toJavaArrayValue( Field field, Class<?> classType) throws JsonValueParseException, ClassNotFoundException, InstantiationException, IllegalAccessException {
       // List<JsonValue> list = values;

        Class componentType = classType.getComponentType();
        Object array = Array.newInstance(componentType, values.size());

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
        for (JsonValue v : values) {
            Array.set(array, i, JsonParser.jsonValueToEntity(v, null, componentType, gTypes));
            i++;
        }
        return array;
    }

     Collection<Object> toJavaCollectionValue( Field field, Class rawClass, Type genericType) throws IllegalAccessException, InstantiationException, JsonValueParseException, ClassNotFoundException {
        // List<JsonValue> jsonValues = jsonValue.getValue();
        Collection<Object> listObject = null;

        if (rawClass.isInterface() || Modifier.isAbstract(rawClass.getModifiers())) {
            listObject = new ArrayList<>();
        } else {
            listObject = (Collection<Object>) rawClass.newInstance();
        }

        if (genericType instanceof Class) { // Not
            Class gType = (Class) genericType;
            for (JsonValue val : values) {
                listObject.add(JsonParser.jsonValueToEntity(val, null, gType, null));
            }
            return listObject;
        } else if (genericType instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) genericType;
            Class rClass = (Class) pType.getRawType();
            Type[] types = pType.getActualTypeArguments();
            for (JsonValue val : values) {
                listObject.add(JsonParser.jsonValueToEntity(val, null, rClass, types));
            }
            return listObject;
        } else {
            throw new JsonValueParseException("Cannot cast JSON to" + rawClass);
        }
    }
}
