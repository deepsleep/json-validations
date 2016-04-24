package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/19/16.
 */

import com.jfcheng.json.parse.exception.JsonObjectParseException;
import com.jfcheng.json.parse.exception.JsonValueParseExeception;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class JsonObject {
    private Map<String, Object> values;

    public JsonObject(Map<String, Object> values) {
        this.values = values;
    }

    // look for the end of object }
    public static JsonObject parse(Reader reader) throws IOException, JsonValueParseExeception {

        Map<String, Object> values = new HashMap<String, Object>();
        int val = reader.read();
        boolean isObjectEndWasFound = false;
        while (val != -1) {
            char c = (char) val;

            if (JsonControlChar.isWhitespaceChar(c)) {
                val = reader.read(); // skip all the whitespace('','\t','\n','r'). continue
            } else if (JsonControlChar.isObjectEnd(c)) {
                isObjectEndWasFound = true;
                break;
            } else if (JsonControlChar.isValueSeparator(c)) {
                if (values.size() == 0) {
                    throw new JsonObjectParseException("Paring object fail: invalid object structure '{,'");  // { , exception.
                } else {
                    int tmpVal = addKVPair(values, reader, JsonControlChar.MEANINGLESS_CHAR); // look for a k-v
                    if (tmpVal != JsonControlChar.MEANINGLESS_CHAR) {
                        val = tmpVal;
                    } else {
                        val = reader.read();
                    }
                }
            } else if (values.size() == 0) {
                int tmpVal = addKVPair(values, reader, val);
                if (tmpVal != JsonControlChar.MEANINGLESS_CHAR) {
                    val = tmpVal;
                } else {
                    val = reader.read();
                }
            } else {
                throw new JsonObjectParseException("Invalid json object structure: invalid char was got: " + c);
            }
        }

        if (isObjectEndWasFound == true) {
            return new JsonObject(values);
        } else {
            throw new JsonObjectParseException("Json object parsing error: Unclosed object. Expecting ',' or ']',  but got 'EOF' ");
        }

    }

    private static int addKVPair(Map<String, Object> values, Reader reader, int lastReadChar) throws IOException, JsonValueParseExeception {
        JsonParserResult returnValue = JsonKVPair.parse(reader, lastReadChar);
        JsonKVPair kvPair = (JsonKVPair) returnValue.getValue();
        // check if the key is duplicated
        if (values.containsKey(kvPair.getName())) {
            throw new JsonObjectParseException("Duplicated key in the json object: " + kvPair.getName());
        } else {
            values.put(kvPair.getName(), kvPair.getValue());
            return returnValue.getLastCharRead();
        }
    }

    public Map<String, Object> getValues() {
        return values;
    }

}
