package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/20/16.
 */
public class JsonParserResult {
   private Object value;
   private int lastCharRead;

    public JsonParserResult(Object value, int controlChar) {
        this.value = value;
        this.lastCharRead = controlChar;
    }

    public Object getValue() {
        return value;
    }

    public int getLastCharRead() {
        return lastCharRead;
    }
}
