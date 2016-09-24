package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

/**
 * Created by jfcheng on 9/2/16.
 */
public class JsonWriter {
    private final Writer out;

    private  String indent = "    "; // 4 space

    private char newLineChar = '\n';

    public JsonWriter(Writer out) {
        this.out = out;
    }

    public JsonWriter(){
        out = new StringWriter();
    }

    public JsonWriter(boolean isPrettyPrint){
        out = new StringWriter();
        if(!isPrettyPrint){
            indent = "";
            newLineChar = ' ';
        }
    }


    public void writeJasonValue(JsonValue jsonValue) throws IOException, JsonValueParseException {
        writeJasonValue(jsonValue,0,false);
    }

    protected void writeJasonValue(JsonValue jsonValue, int indent, boolean isAfterValueSeparator) throws IOException, JsonValueParseException {
        if(jsonValue instanceof JsonNull ||jsonValue instanceof JsonBoolean ||
                jsonValue instanceof JsonNumber){
            out.write(jsonValue.toString());
        } else if(jsonValue instanceof JsonString){
            out.write(jsonValue.toString());
        }else if(jsonValue instanceof JsonArray){
            JsonArray array = (JsonArray) jsonValue;
            writeJsonArray(array,indent, isAfterValueSeparator);
        } else if(jsonValue instanceof JsonObject){
            JsonObject obj = (JsonObject) jsonValue;
            writeJsonObject(obj,indent,isAfterValueSeparator);
        }else{
            // do nothing.
        }
    }


    protected  void writeJsonObject(JsonObject obj, int indentNumber, boolean isAfterValueSeparator) throws JsonValueParseException, IOException {
       if(!isAfterValueSeparator) {
           writeIndents(indentNumber);
       }

        if(obj.getValue() == null || obj.getValue().isEmpty()){
            out.write("{}");
        }else{

            out.write('{');
            out.write(newLineChar);

            int newIntentNumber = indentNumber + 1;

            JsonObject jsonValue = (JsonObject) obj;
            Map<JsonString,JsonValue> values = jsonValue.getValue();
            Set<JsonString> keys = values.keySet();

            boolean needToIndent;
            int counter = 0;
            for(JsonString key: keys){
                writeKey(key.getValue(),newIntentNumber);

                JsonValue val = values.get(key);
                if( !(val instanceof JsonObject) && !(val instanceof JsonArray)){
                   // writeIndents(newIntentNumber);
                    needToIndent =false;
                }else{
                    needToIndent = true;
                }

                writeJasonValue(val,newIntentNumber,needToIndent);
                counter ++;
                if(counter < keys.size()){
                    out.write(',');
                }
                out.write(newLineChar);
            }

            writeIndents(indentNumber);
            //out.write(newLineChar);
            out.write('}');
        }
    }

    protected void writeJsonArray(JsonArray array, int indentNumber, boolean isAfterValueSeparator) throws IOException, JsonValueParseException {
        if(!isAfterValueSeparator) {
            writeIndents(indentNumber);
        }
        if(array.getValue() == null || array.getValue().isEmpty()){
            out.write("[]");
        }else{

            out.write('[');
            out.write(newLineChar);

            int newIntentNumber = indentNumber + 1;

            for(int i=0; i< array.getValue().size(); i++){
                JsonValue val = array.getValue().get(i);

                if( !(val instanceof JsonObject) && !(val instanceof JsonArray)){
                    writeIndents(newIntentNumber);
                    //isFirstValue =false;
                }

                writeJasonValue(array.getValue().get(i),newIntentNumber,false);

                if(i < (array.getValue().size() -1)){
                    out.write(',');
                }
                out.write(newLineChar);
            }

            writeIndents(indentNumber);
            out.write(']');

        }
    }

    protected void writeIndents(int indentNumber) throws IOException {
        for(int i=0; i < indentNumber; i++){
            out.write(indent);
        }
    }

    public void writeKey(String key,int indentNumber) throws IOException {
        writeIndents(indentNumber);
        out.write("\""+key+"\": " );
    }



    @Override
    public String toString() {
        return out.toString();
    }
}
