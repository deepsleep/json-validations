package com.jfcheng.json.parse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

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

}
