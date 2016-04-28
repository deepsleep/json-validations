package com.jfcheng.json.parse;

import com.google.gson.JsonParseException;
import com.jfcheng.utils.DataConversionUtils;

import java.lang.reflect.Field;


/**
 * Created by jfcheng on 4/24/16.
 */
public class JsonNumber implements JsonValue {
    private static final long serialVersionUID = -7615334752227411611L;

    private Number value;

    public JsonNumber(Number value) {
        this.value = value;
    }

    @Override
    public Number getValue() {
        return value;
    }

    @Override
    public Number toJsonText(boolean ignoreNullField) {
        return value;
    }


    public static JsonValue toJsonValue(Object obj) throws JsonParseException {
        if (obj == null) {
            return new JsonNull();
        } else if (obj instanceof JsonValue) {
            return (JsonNumber) obj;
        } else if (obj instanceof Number) {
            return new JsonNumber((Number) obj);
        } else {
            throw new JsonParseException("Cannot cast " + obj.getClass() + " to JsonNumber");
        }
    }

//    @Override
//    public int hashCode() {
//        return value.hashCode();
//    }
//
//    public boolean equals(Object o) {
//        if (o == this) {
//            return true;
//        } else if (o == null || !(o instanceof JsonNumber)) {
//            return false;
//        } else {
//            return value.equals(o);
//        }
//    }

    @Override
    public String toString() {
        return String.valueOf(toJsonText(DEFAULT_IGNORE_NULL_FIELD));
    }


    Object toJavaNumberValue(Field field, Class<?> clazz) {
        return DataConversionUtils.numberToPrimitive(value, clazz);
    }
}
