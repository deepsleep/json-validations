package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/19/16.
 */

import com.jfcheng.json.parse.exception.JsonObjectParseException;
import com.jfcheng.json.parse.exception.JsonValueParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonObject implements JsonValue {
    private static final long serialVersionUID = 5521558531152229557L;

    private Map<JsonString, JsonValue> values;

    public JsonObject() {
        this.values = new HashMap<JsonString, JsonValue>();
    }

    public JsonObject(Map<JsonString, JsonValue> values) {
        this.values = values;
    }

    public static JsonObject parse(Reader reader) throws IOException, JsonValueParseException {

        Map<JsonString, JsonValue> values = new HashMap<JsonString, JsonValue>();
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

    private static int addKVPair(Map<JsonString, JsonValue> values, Reader reader, int lastReadChar) throws IOException, JsonValueParseException {
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


    @Override
    public JsonObject getValue() {
        return this;
    }

    @Override
    public String toJsonText() {
        if (values == null) {
            return "null";
        } else {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(JsonControlChar.OBJECT_BEGIN);
            Set<JsonString> keys = values.keySet();

            int counter = 1;
            for (JsonString key : keys) {
                strBuilder.append(key.toJsonText());
                strBuilder.append(JsonControlChar.NAME_SEPARATOR);
                strBuilder.append(values.get(key).toJsonText());
                if (counter < values.size()) {
                    strBuilder.append(JsonControlChar.VALUE_SEPARATOR);
                }
                counter += 1 ;
            }

            strBuilder.append(JsonControlChar.OBJECT_END);
            return strBuilder.toString();
        }
    }

}
