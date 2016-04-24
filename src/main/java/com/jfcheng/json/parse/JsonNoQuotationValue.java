package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseExeception;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

/**
 * Created by jfcheng on 4/22/16.
 */
public class JsonNoQuotationValue {
    private static final String TRUE_VALUE = "true";
    private static final String FALSE_VALUE = "false";
    private static final String NULL_VALUE = "null";

    private static final String NUMBER_GRAMMAR = "[-]?((0(\\.([0-9]+))?)|([1-9][0-9]*(\\.([0-9]+))?))(([eE][+-]?([0-9]+))?)";
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_GRAMMAR);

    public static JsonParserResult parse(Reader reader, int lastReadChar) throws JsonValueParseExeception, IOException {
        StringBuilder stringBuilder = new StringBuilder();

        int val = lastReadChar;

        while (val !=JsonControlChar.END_OF_READ && !JsonControlChar.isEndOfNoQuotationValue((char) val)) {
            char c = (char) val;
            stringBuilder.append(c);
            val = reader.read();
        }

        String strVal = stringBuilder.toString();
        Object value;
        if (TRUE_VALUE.equals(strVal)) {
            value = true;
        } else if (FALSE_VALUE.equals(strVal)) {
            value = false;
        } else if (NULL_VALUE.equals(strVal)) {
            value = null;
        } else if (isNumberValue(strVal)) {
            value = parseNumber(strVal);
        } else {
            throw new JsonValueParseExeception("Parsing fail, invalid no quotation value: " + strVal);
        }

        return new JsonParserResult(value, val);
    }

    public static boolean isNumberValue(String str) {
        if (str == null) {
            return false;
        } else {
            return NUMBER_PATTERN.matcher(str).matches();
        }
    }

    public static final Number parseNumber(String str) {
        if (str.contains(".") || str.contains("e") || str.contains("E")) {
            return Double.valueOf(str);
        } else {
            return Long.valueOf(str);
        }
    }
}
