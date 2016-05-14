package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseException;
import com.jfcheng.json.annotation.AnnotationHelper;
import com.jfcheng.json.annotation.exception.InvalidParameterValueException;
import com.jfcheng.json.annotation.exception.RequiredFieldNotFoundException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jfcheng on 4/19/16.
 * <p>
 * This is a JSON parser according to the rfc7159.
 */
public class JsonParser {
    private JsonParser() {
    }

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
                new JsonValueParseException("Unexpected internal IOException" + e.getMessage());
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

            reader.close();
            return value;
        }
    }


    public static JsonValue toJsonValue(Object obj) throws JsonValueParseException {
            if (obj == null) {
                return JsonNull.toJsonValue(obj);
            } else if (obj instanceof JsonValue) {
                return (JsonValue) obj;
            } else if (obj instanceof String || obj instanceof Character || obj instanceof Enum) {
                return JsonString.toJsonValue(obj);
            } else if (obj instanceof Number) {
                return JsonNumber.toJsonValue(obj);
            } else if (obj instanceof Boolean) {
                return JsonBoolean.toJsonValue(obj);
            } else if (obj.getClass().isArray()) {
                return JsonArray.toJsonValue(obj);
            } else if (obj instanceof List) {
                return JsonArray.toJsonValue(obj);
            } else if (obj instanceof Set) {
                return JsonArray.toJsonValue(obj);
            } else if (obj instanceof Map) {
                return JsonObject.toJsonValue(obj);
            } else {
                return JsonObject.toJsonValue(obj);
            }
    }

    static JsonParserResult parse(Reader reader, int lastCharRead) throws JsonValueParseException, IOException {
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

    static Object jsonValueToEntity(JsonValue jsonValue, Field field, String fieldName, Class rawClass, Type[] parameterTypes, boolean doValidation) throws JsonValueParseException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvalidParameterValueException, RequiredFieldNotFoundException {
        if (field != null && AnnotationHelper.isJsonToEntityIgnore(field)) {
            return null;
        } else {
            if (jsonValue instanceof JsonNull) {
                return null;
            } else if (jsonValue instanceof JsonBoolean) {
                JsonBoolean jBoolean = (JsonBoolean) jsonValue;
                return jBoolean.toJavaBooleanValue(field, doValidation);
            } else if (jsonValue instanceof JsonNumber) {
                JsonNumber jNumber = (JsonNumber) jsonValue;
                return jNumber.toJavaNumberValue(field, fieldName, rawClass, doValidation);
            } else if (jsonValue instanceof JsonString) {
                JsonString jString = (JsonString) jsonValue;
                return jString.toJavaStringValue(field, fieldName, rawClass, doValidation);
            } else if (jsonValue instanceof JsonArray) {
                JsonArray jArray = (JsonArray) jsonValue;
                if (rawClass.isArray()) {
                    return jArray.toJavaArrayValue(field, fieldName, rawClass, doValidation);
                } else if (Collection.class.isAssignableFrom(rawClass) && parameterTypes != null && parameterTypes.length == 1) {
                    return jArray.toJavaCollectionValue(field, fieldName, rawClass, parameterTypes[0], doValidation);
                } else {
                    throw new JsonValueParseException("Cannot cast to class " + rawClass + " with types " + parameterTypes);
                }
            } else if (jsonValue instanceof JsonObject) {
                JsonObject jObject = (JsonObject) jsonValue;
                if (Map.class.isAssignableFrom(rawClass)) {
                    if (parameterTypes != null && parameterTypes.length == 2) {
                        return jObject.toJavaMapValue(field, fieldName, rawClass, parameterTypes[0], parameterTypes[1], doValidation);
                    } else {
                        throw new JsonValueParseException("Cannot cast to class " + rawClass + " with types " + parameterTypes);
                    }
                } else {
                    return jObject.toOtherObjectValue(field, fieldName, rawClass, doValidation);
                }
            } else {
                throw new JsonValueParseException("Cannot cast " + jsonValue.getValue() + " to " + rawClass);
            }
        }

    }


}
