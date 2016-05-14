package com.jfcheng.json.parse;


import com.jfcheng.json.parse.exception.JsonValueParseException;

/**
 * Created by jfcheng on 4/24/16.
 */
public class JsonNull implements JsonValue {

    private static final long serialVersionUID = 7933287137363380388L;

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String toJsonText(boolean ignoreNullField) {
        return "null";
    }


    public static JsonValue toJsonValue(Object obj) throws JsonValueParseException {
        if (obj instanceof JsonNull) {
            return (JsonNull) obj;
        } else if (obj == null) {
            return new JsonNull();
        } else {
            throw new JsonValueParseException("Cannot cast " + obj.getClass() + " to JsonNull");
        }
    }


    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof JsonNull)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return toJsonText(DEFAULT_IGNORE_NULL_FIELD);
    }

}
