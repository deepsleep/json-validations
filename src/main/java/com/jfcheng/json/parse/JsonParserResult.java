package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/20/16.
 */
public class JsonParserResult {
    private JsonValue value;
    private int lastCharRead;

    public JsonParserResult(JsonValue value, int controlChar) {
        this.value = value;
        this.lastCharRead = controlChar;
    }

    public JsonValue getValue() {
        return value;
    }

    public int getLastCharRead() {
        return lastCharRead;
    }
}
