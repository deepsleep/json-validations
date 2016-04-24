package com.jfcheng.validations.test.entity;

import com.jfcheng.validation.annotation.*;


/**
 * Created by jfcheng on 2/27/16.
 */
public class TestBase {
    @ValidNumbers(value={11,12,21})
    @RangeValue(min=10, max=20)
    private int baseInt;

    @JsonIgnore(request = true, response = false)
    @ValidLength(min=3,max=5)
    @ValidStrings({"abc","123"})
    private String baseStr;

    @JsonName("is_true")
    @JsonIgnore(request = false, response = true)
    private boolean isTrue = false;

    @Required(false)
    protected String notUsed;

    private EnumTest enum1 = EnumTest.ONE;

    public TestBase(){

    }

    public TestBase(int intField, String strField) {
        this.baseInt = intField;
        this.baseStr = strField;
    }

    public int getBaseInt() {
        return baseInt;
    }

    public void setBaseInt(int baseInt) {
        this.baseInt = baseInt;
    }

    public String getBaseStr() {
        return baseStr;
    }

    public void setBaseStr(String baseStr) {
        this.baseStr = baseStr;
    }
}
