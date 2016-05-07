package com.jfcheng.json.test;

import java.io.*;

/**
 * Created by jfcheng on 4/16/16.
 */
public class FileReader {

    public static BufferedReader getBufferReader(String path) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader;
    }

    public static String readToString(String path){
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
    }

}
