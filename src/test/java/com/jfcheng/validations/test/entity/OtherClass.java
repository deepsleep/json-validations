package com.jfcheng.validations.test.entity;

import com.jfcheng.validation.annotation.JsonIgnore;
import com.jfcheng.validation.annotation.ValidStrings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jfcheng on 2/27/16.
 */
public class OtherClass {

    @JsonIgnore(request = false, response = true)
    @ValidStrings({"abc","123"})
    private String otherStr;

    private Map<String,String> otherMap = new HashMap<String,String>();

    public String getOtherStr() {
        return otherStr;
    }

    public void setOtherStr(String otherStr) {
        this.otherStr = otherStr;
    }

    public OtherClass(){
        otherMap.put("abc", "dddddd");
        otherMap.put("123", "ffffff");
    }

    public OtherClass(String otherStr) {
        this.otherStr = otherStr;
        otherMap.put("abc", "dddddd");
        otherMap.put("123", "ffffff");
    }
}
