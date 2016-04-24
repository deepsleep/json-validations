package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseException;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by jfcheng on 4/19/16.
 */
public class JsonKVPair implements JsonValue{
    private static final long serialVersionUID = 2479919232122425290L;

    private JsonString name;
    private JsonValue value;

    public JsonKVPair(JsonString name, JsonValue value) {
        this.name = name;
        this.value = value;
    }

    public JsonString getName() {
        return name;
    }

    public JsonValue getValue() {
        return value;
    }

    @Override
    public String toJsonText() {
        return name.toJsonText() + JsonControlChar.NAME_SEPARATOR + value.toJsonText();
    }

    public static JsonParserResult parse(Reader reader, int lastCharRead) throws IOException, JsonValueParseException {
        JsonString name = null;
        JsonValue value = null;
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
                    throw new JsonValueParseException("Key should be a quotation string: unexpected char '" + c + "' was found.");
                }
            }
        }
        return new JsonParserResult(new JsonKVPair(name, value), lastCharRead);
    }
}
