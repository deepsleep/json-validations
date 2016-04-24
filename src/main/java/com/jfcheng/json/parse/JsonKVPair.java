package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseExeception;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by jfcheng on 4/19/16.
 */
public class JsonKVPair {
    private String name;
    private Object value;

    public JsonKVPair(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public static JsonParserResult parse(Reader reader, int lastCharRead) throws IOException, JsonValueParseExeception {
        String name = null;
        Object value = null;
        int val = lastCharRead;

       // int returnControlChar = JsonControlChar.MEANINGLESS_CHAR;

        if(val == JsonControlChar.MEANINGLESS_CHAR){
            val = reader.read();  // read a char if lastCharRead is meaningless
        }

        while (val != JsonControlChar.END_OF_READ) {
            char c = (char) val;

            if (JsonControlChar.isWhitespaceChar(c)) {
                val = reader.read();  // skip all the whitespace('','\t','\n','r'). continue
            } else {
                if (JsonControlChar.isStringBegin(c)) {
                    name = JsonString.parse(reader);  // parse String value;
                    val = reader.read();
                } else if (c == JsonControlChar.NAME_SEPARATOR && name != null) {
                    JsonParserResult childObject = JsonParser.parse(reader, JsonControlChar.MEANINGLESS_CHAR);
                    value = childObject.getValue();
                    lastCharRead = childObject.getLastCharRead();

                    break; // break the while loop.
                } else {
                    throw new JsonValueParseExeception("Key should be a quotation string: unexpected char '" + c + "' was found.");
                }
            }
        }
        return new JsonParserResult(new JsonKVPair(name, value), lastCharRead);
    }
}
