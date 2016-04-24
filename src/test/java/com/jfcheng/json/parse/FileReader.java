package com.jfcheng.json.parse;

import java.io.*;
import java.nio.file.Paths;

/**
 * Created by jfcheng on 4/16/16.
 */
public class FileReader {

    public static BufferedReader getBufferReader(String path){
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader =  new BufferedReader(new InputStreamReader(in));
        return reader;
    }

    public static void main(String[] args) throws IOException {
        Reader reader = getBufferReader("/Users/jfcheng/IdeaProjects/ObjectValidations/JsonObjectValidations/src/main/resources/json-parser/JsonAllowWhiteSpaces");
        int i;
        while((i = reader.read()) != -1){
            //
        }
        System.out.println("end of read");
        System.out.println(reader.read());
        System.out.println(reader.read());
    }

}
