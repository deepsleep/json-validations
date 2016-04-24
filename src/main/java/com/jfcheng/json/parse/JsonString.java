package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonStringParseException;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by jfcheng on 4/18/16.
 */
public class JsonString {
    private static final char[] ALLOW_ESCAPE_CHARS = {'"', '\\', '/', 'b', 'f', 'n', 'r', 't', 'u'};
    private static final char[] NOT_ALLOWED_CHARS = {'\n', '\r', '\t'}; // not allow new line char or tab in the string value

    private static final char UNICODE_0 = 48;  // 0, \u0030
    private static final char UNICODE_9 = 57;  // 9, \u0039
    private static final char UNICODE_A = 65;  // A, \u0041
    private static final char UNICODE_F = 70;  // F, \u0046
    private static final char UNICODE_a = 97;  // a, \u0061
    private static final char UNICODE_f = 102; // f, \u0066


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
    public static String parse(Reader reader) throws IOException, JsonStringParseException {

        StringBuilder stringBuilder = new StringBuilder();  // start quotation.

        // boolean hasEscapeMarker = false;
        //   StringBuilder unicodeValues = null;


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

//
//
//            else {
//                if (hasEscapeMarker) {
//                    if (isAllowEscapeChar(c)) {
//                        stringBuilder.append(c);
//                        if(c != 'u'){
//                             hasEscapeMarker = false;
//                        } // else for remain unicode value
//                    } else if (isValidUnicodeChar(c)) {
//                        if (unicodeValues == null) {
//                            unicodeValues = new StringBuilder(4);
//                        }
//                        unicodeValues.append(c);
//
//                        if (unicodeValues.length() == 4) {
//                            stringBuilder.append(unicodeValues);
//                            unicodeValues = null;       // reset unicode value
//                            hasEscapeMarker = false;    // reset escape marker
//                        }
//
//                    } else {
//                        throw new JsonStringParseException("Invalid unicode value: " + c + " in " + stringBuilder);
//                    }
//
//                } else {
//                    if (c == '\\') {
//                        hasEscapeMarker = true;
//                        stringBuilder.append(c);
//                    } else if (c == '"') { // end of string
//                        foundEndOfStringQuotation = true;
//                        break;        // break the while loop to stop searching.
//                    } else {
//                        stringBuilder.append(c);
//                    }
//                }
//            }
//        }

        if (foundEndOfStringQuotation) {
            return stringBuilder.toString();
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

}
