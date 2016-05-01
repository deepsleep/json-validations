package com.jfcheng.json.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jfcheng on 4/26/16.
 */
public class Student {
    char charValue;

    private String name;
    private Short age;
    private short height;
    private int[] luckyNumber;
    private List<Student> classmates;
    private List<String> courses;
    private Map<String,Integer> scores;
    private List<String> emptyStr = new ArrayList<String>();
    private List<Student> emptyStu = new ArrayList<Student>();
    private Map<String,Integer> emptyMap = new HashMap<String,Integer>();
    private List<String> nullList;



    Student(){

    }

    Student(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public short getAge() {
        return age;
    }


    public int[] getLucyNumber() {
        return luckyNumber;
    }

    public List<Student> getClassmates() {
        return classmates;
    }

    public List<String> getCourses() {
        return courses;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }




}
