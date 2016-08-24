package com.jfcheng.json.test;

import java.io.*;

/**
 * Created by jfcheng on 4/16/16.
 */
public class FileReaderUtils {

    public static BufferedReader getBufferReader(String path) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return reader;
    }

    public static String readToString(String path){
        return readToString2(path);
        /*
        BufferedReader reader = getBufferReader(path);
        String line ;
       StringBuilder stringBuilder = new StringBuilder();
        try {
            while((line = reader.readLine()) !=null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
        */
    }

    public static String readToString2(String path){
        String str = null;
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(path))) {
            StringBuilder  strBuilder = new StringBuilder();
            String line;
            while((line = reader.readLine()) !=null){
                strBuilder.append(line);
            }

            str = strBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }

}
