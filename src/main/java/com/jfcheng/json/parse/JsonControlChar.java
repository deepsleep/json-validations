package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/19/16.
 */
public class JsonControlChar {
    // Allow whitespaces: Space,horizontal tab,new line & carriage return.
    private static final char[] WHITESPACES = {'\u0020', '\t', '\n', '\r'};

    // Six structural characters: []{}:,
    public static final char ARRAY_BEGIN = '[';
    public static final char ARRAY_END = ']';
    public static final char OBJECT_BEGIN = '{';
    public static final char OBJECT_END = '}';
    public static final char NAME_SEPARATOR = ':';
    public static final char VALUE_SEPARATOR = ',';

    public static final char QUOTATION_MARK = '"'; // "
    public static final int END_OF_READ = -1;

    public static final int MEANINGLESS_CHAR = -2;


    public static boolean isWhitespaceChar(char cValue) {
        boolean isWhitespace = false;
        for (char c : WHITESPACES) {
            if (c == cValue) {
                isWhitespace = true;
                break;
            }
        }
        return isWhitespace;
    }


    public static boolean isArrayBegin(char c) {
        if (c == ARRAY_BEGIN) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isArrayEnd(char c) {
        if (c == ARRAY_END) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isObjectBegin(char c) {
        if (c == OBJECT_BEGIN) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isObjectEnd(char c) {
        if (c == OBJECT_END) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValueSeparator(char c) {
        if (c == VALUE_SEPARATOR) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isStringBegin(char c) {
        if (c == QUOTATION_MARK) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEndOfNoQuotationValue(char c) {
        if (c == VALUE_SEPARATOR || isArrayEnd(c) || isObjectEnd(c) || isWhitespaceChar(c)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMeaninglessChar(int charInt) {
        if (charInt == MEANINGLESS_CHAR) {
            return false;
        } else {
            return true;
        }
    }
}
