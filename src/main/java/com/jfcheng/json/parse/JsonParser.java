package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by jfcheng on 4/19/16.
 * <p>
 * This is a JSON parser according to the rfc7159.
 */
public class JsonParser {
    public static JsonValue parseString(String string) throws JsonValueParseException {
        if (string == null) {
            return new JsonNull();
        } else if (string.trim().length() == 0) {
            return new JsonString("");
        } else {
            Reader reader = new StringReader(string);
            JsonValue jsonObject = null;
            try {
                jsonObject = parse(reader);
            } catch (IOException e) {
                new JsonValueParseException("Unexpected IOException" + e.getMessage());
            }
            return jsonObject;
        }
    }

    public static JsonValue parse(Reader reader) throws IOException, JsonValueParseException {
        if (reader == null) {
            return null;
        } else {
            JsonParserResult returnObject = parse(reader, JsonControlChar.MEANINGLESS_CHAR);
            JsonValue value = returnObject.getValue();

            // check remain chars
            int remainChar = returnObject.getLastCharRead();
            if (!JsonControlChar.isMeaninglessChar(remainChar)) {
                remainChar = reader.read();
            }

            while (remainChar != JsonControlChar.END_OF_READ) {
                char c = (char) remainChar;
                if (!JsonControlChar.isWhitespaceChar(c)) {
                    throw new JsonValueParseException("Json parsing error: unexpected char '" + c + "' was found.");
                } else {
                    remainChar = reader.read();
                }
            }
            return value;
        }
    }

    public static JsonParserResult parse(Reader reader, int lastCharRead) throws JsonValueParseException, IOException {
        // value should be  false / null / true / object / array / number / string
        JsonValue jsonValue = null;

        if (lastCharRead == JsonControlChar.MEANINGLESS_CHAR) {
            lastCharRead = reader.read(); // if lastCharRead is -2, meaning it's not useful, just ignore it.
        }
        int val = lastCharRead;
        int controlChar = JsonControlChar.MEANINGLESS_CHAR;

        while (val != JsonControlChar.END_OF_READ) {
            char c = (char) val;

            if (JsonControlChar.isWhitespaceChar(c)) {
                val = reader.read();   // skip all the whitespace('','\t','\n','r'). continue
            } else if (JsonControlChar.isStringBegin(c)) {
                jsonValue = JsonString.parse(reader);          // type of string
                break;
            } else if (JsonControlChar.isArrayBegin(c)) {
                jsonValue = JsonArray.parse(reader);           // type of array
                break;
            } else if (JsonControlChar.isObjectBegin(c)) {
                jsonValue = JsonObject.parse(reader);          // type of object
                break;
            } else {
                JsonParserResult returnObject = JsonNoQuotationValue.parse(reader, val);  // type of boolean, null and number
                jsonValue = returnObject.getValue();
                controlChar = returnObject.getLastCharRead();
                break;
            }
        }

        return new JsonParserResult(jsonValue, controlChar);
    }


}
