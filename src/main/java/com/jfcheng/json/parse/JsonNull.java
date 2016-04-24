package com.jfcheng.json.parse;

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


}
