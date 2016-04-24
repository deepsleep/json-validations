package com.jfcheng.validations.test.entity;

import com.jfcheng.validation.annotation.JsonIgnore;

import java.util.*;

/**
 * Created by jfcheng on 2/27/16.
 */
public class TestDerivedClass extends TestBase {

    private String derivedStr;

    private OtherClass other;

    private Integer integer;

    private Long longField =12L;

    private int[] intArray = {1, 2};

    @JsonIgnore(request = false, response = true)
    private long[] longArray = {11L,22L};

    private String[] strArray = {"aa", "bb"};

    private OtherClass[] otherArray = {new OtherClass("O1"), new OtherClass("O2")};

    private static String staticField = "abcddd";
    private List<String> list = new ArrayList<String>(Arrays.asList(strArray));

    List<OtherClass> otherList = new ArrayList<OtherClass>(Arrays.asList(otherArray));

    Map<OtherClass, OtherClass> mapValue = new HashMap();

    public TestDerivedClass(int i, String f, String a, OtherClass other,Integer i2) {
        super(i, f);
        this.derivedStr = a;
        this.other = other;
        integer = i2;
        mapValue.put(otherArray[0],otherArray[1]);
    }

    public TestDerivedClass() {


    }

    public String getDerivedStr() {
        return derivedStr;
    }

    public void setDerivedStr(String derivedStr) {
        this.derivedStr = derivedStr;
    }

    public OtherClass getOther() {
        return other;
    }

    public void setOther(OtherClass other) {
        this.other = other;
    }
}
