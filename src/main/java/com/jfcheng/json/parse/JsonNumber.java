package com.jfcheng.json.parse;

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
    public Number toJsonText() {
        return value;
    }
}
