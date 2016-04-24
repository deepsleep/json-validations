package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/24/16.
 */
public class JsonBoolean implements JsonValue{
    private static final long serialVersionUID = 5045495097922600307L;

    private boolean value;

    public JsonBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public Boolean toJsonText() {
        return value;
    }


}
