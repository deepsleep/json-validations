package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/20/16.
 */
class JsonParserResult {
    private JsonValue value;
    private int lastCharRead;

    JsonParserResult(JsonValue value, int controlChar) {
        this.value = value;
        this.lastCharRead = controlChar;
    }

    JsonValue getValue() {
        return value;
    }

    int getLastCharRead() {
        return lastCharRead;
    }
}
