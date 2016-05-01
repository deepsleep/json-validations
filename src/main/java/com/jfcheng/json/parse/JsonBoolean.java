package com.jfcheng.json.parse;

import com.google.gson.JsonParseException;

import java.lang.reflect.Field;

/**
 * Created by jfcheng on 4/24/16.
 */
public class JsonBoolean implements JsonValue {
    private static final long serialVersionUID = 5045495097922600307L;

    private Boolean value;

    public JsonBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public Boolean toJsonText(boolean ignoreNullField) {
        return value;
    }

    public static JsonValue toJsonValue(Object obj) throws JsonParseException {
        if (obj == null) {
            return new JsonNull();
        } else if (obj instanceof JsonBoolean) {
            return (JsonBoolean) obj;
        } else if (obj instanceof Boolean) {
            return new JsonBoolean((Boolean) obj);
        } else {
            throw new JsonParseException("Cannot cast " + obj.getClass() + " to JsonBoolean");
        }
    }

    @Override
    public String toString() {
        return String.valueOf(toJsonText(DEFAULT_IGNORE_NULL_FIELD));
    }


    Object toJavaBooleanValue(Field field, boolean doValidation) {
        if (field != null && doValidation) {
            //DO some validation if need.
        }

        return value;
    }


    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !(o instanceof JsonBoolean)) {
            return false;
        } else {
            return value.equals(((JsonBoolean) o).getValue());
        }
    }

}
