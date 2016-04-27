package com.jfcheng.json.parse;

/**
 * Created by jfcheng on 4/26/16.
 */
public class Student {
    private String name;
    private Short age;
    private int[] lucyNumber;

    public String getName() {
        return name;
    }

    public short getAge() {
        return age;
    }

    public void printLuckyNumber(){
        System.out.print("lucyNumber: ");
        for(int i: lucyNumber){
            System.out.print(i + ",");
        }
        System.out.println();
    }
}
