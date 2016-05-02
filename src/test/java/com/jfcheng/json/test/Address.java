package com.jfcheng.json.test;

/**
 * Created by jfcheng on 4/25/16.
 */
public class Address {
    private String phone;
    private String road;

    public Address(){}
    public Address(String phone, String road) {
        this.phone = phone;
        this.road = road;
    }

    public String getPhone() {
        return phone;
    }

    public String getRoad() {
        return road;
    }
}
