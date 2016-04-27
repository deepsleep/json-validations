package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonStringParseException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;

/**
 * According to rfc7159, a json string is a string which begins and ends with a quotation mark(").
 * <p>string = quotation-mark *char quotation-mark</p>
 * <p>All Unicode characters may be placed within the quotation marks, except for the characters that must be escaped:
 * quotation mark, reverse solidus, and the control characters (U+0000 through U+001F).
 * </p>
 * <p>
 * <p>Created by jfcheng on 4/18/16.</p>
 */
public class JsonString implements JsonValue {
    private static final long serialVersionUID = -4407999813436975142L;

    private static final char[] ALLOW_ESCAPE_CHARS = {'"', '\\', '/', 'b', 'f', 'n', 'r', 't', 'u'};
    private static final char[] NOT_ALLOWED_CHARS = {'\n', '\r', '\t'}; // not allow new line char or tab in the string value

    private static final char UNICODE_0 = 48;  // 0, \u0030
    private static final char UNICODE_9 = 57;  // 9, \u0039
    private static final char UNICODE_A = 65;  // A, \u0041
    private static final char UNICODE_F = 70;  // F, \u0046
    private static final char UNICODE_a = 97;  // a, \u0061
    private static final char UNICODE_f = 102; // f, \u0066

    private String value;

    JsonString(String s) {
        value = s;
    }

    private static boolean isAllowEscapeChar(char value) {
        boolean isAllowedChar = false;
        for (char c : ALLOW_ESCAPE_CHARS) {
            if (c == value) {
                isAllowedChar = true;
                break;
            }
        }
        return isAllowedChar;
    }

    private static boolean isValidUnicodeChar(char c) {
        if ((c >= UNICODE_0 && c <= UNICODE_9) || (c >= UNICODE_A && c <= UNICODE_F) || (c >= UNICODE_a && c <= UNICODE_f)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isNotAllowChar(char value) {
        boolean isNotAllowValue = false;
        for (char c : NOT_ALLOWED_CHARS) {
            if (c == value) {
                isNotAllowValue = true;
                break;
            }
        }
        return isNotAllowValue;
    }

    //The reader already has been read the start quotation mark ", parse the string value
    public static JsonString parse(Reader reader) throws IOException, JsonStringParseException {

        StringBuilder stringBuilder = new StringBuilder();  // start quotation.

        // searching for the end quotation and build string value
        boolean foundEndOfStringQuotation = false;
        int val;
        while ((val = reader.read()) != -1) {
            char c = (char) val;
            if (isNotAllowChar(c)) {
                throw new JsonStringParseException("Char '" + c + "' is not allowed in a string");
            } else if (c == '\\') {
                stringBuilder.append(c);
                stringBuilder.append(parseEscapeValue(reader));
            } else if (c == '"') {
                foundEndOfStringQuotation = true;
                break; // Found end of string, break the while loop
            } else {
                stringBuilder.append(c);
            }
        }

        if (foundEndOfStringQuotation) {
            return new JsonString(stringBuilder.toString());
        } else {
            throw new JsonStringParseException("Fail to find the end double quotation \" in " + stringBuilder);
        }
    }


    private static String parseEscapeValue(Reader reader) throws IOException, JsonStringParseException {
        StringBuilder escapeValue = new StringBuilder(5);
        int val = reader.read();
        if (val != JsonControlChar.END_OF_READ) {
            char c = (char) val;
            if (isAllowEscapeChar(c)) {
                escapeValue.append(c);
                if (c == 'u') {
                    escapeValue.append(parseUnicodeValue(reader));
                } // else do nothing.
            } else {
                throw new JsonStringParseException("Invalid escape value: '" + c + "' was found ");
            }

        }
        return escapeValue.toString();
    }

    private static String parseUnicodeValue(Reader reader) throws IOException, JsonStringParseException {
        StringBuilder unicodeValues = new StringBuilder(4);
        int val;

        while (((val = reader.read()) != JsonControlChar.END_OF_READ) && (unicodeValues.length() < 4)) {
            char c = (char) val;
            if (isValidUnicodeChar(c)) {
                unicodeValues.append(c);
            } else {
                throw new JsonStringParseException("Invalid unicode value: '" + c + "' was found.");
            }
        }
        return unicodeValues.toString();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toJsonText() {
        return JsonControlChar.QUOTATION_MARK + value + JsonControlChar.QUOTATION_MARK;
    }

    public static JsonValue toJsonValue(Object obj) throws JsonStringParseException {
        if (obj == null) {
            return new JsonNull();
        } else if (obj instanceof JsonString) {
            return (JsonString) obj;
        } else if (obj instanceof String || obj instanceof Character || obj instanceof Enum) {

            String str = obj.toString();
            // since the parse method need a quotation as the end of the json string.Thus append " to the end of the String.
            String modifiedStr = str + '"';
            Reader reader = new StringReader(modifiedStr);
            try {
                return parse(reader);
            } catch (IOException e) {
                e.printStackTrace(); // This exception should not be
                throw new JsonStringParseException("Unexpected internal exception happen in parsing JsonString");
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new JsonStringParseException("Unexpected internal exception happen in parsing JsonString");
                }
            }
        } else {
            throw new JsonStringParseException("Cannot cast " + obj.getClass() + "to JsonString");
        }
    }


    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || !(obj instanceof JsonString)) {
            return false;
        } else {
            return value.equals(((JsonString) obj).getValue());
        }
    }

    @Override
    public String toString() {
        return toJsonText();
    }



    Object toJavaStringValue( Field field, Class<?> clazz) {
        String strVal = value;
        if (clazz == String.class) {
            return strVal;
        } else if ((clazz == Character.class || clazz == Character.TYPE) && strVal.length() == 1) {
            return strVal.charAt(0);
        } else if (clazz.isEnum()) {
            Class<Enum> eClass = (Class<Enum>) clazz;
            return Enum.valueOf(eClass, strVal);
        } else {
            return strVal;
        }
    }
}
