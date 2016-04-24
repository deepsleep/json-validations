package com.jfcheng.json.parse;

import com.google.gson.JsonParseException;

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
    public String toJsonText() {
        return "null";
    }


    public static JsonValue toJsonValue(Object obj) throws JsonParseException {
        if (obj instanceof JsonNull) {
            return (JsonNull) obj;
        } else if (obj == null) {
            return new JsonNull();
        } else {
            throw new JsonParseException("Cannot cast " + obj.getClass() + " to JsonNull");
        }
    }


    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof JsonNull)) {
            return false;
        } else {
            return o == null;
        }
    }

    @Override
    public String toString() {
        return toJsonText();
    }

}
