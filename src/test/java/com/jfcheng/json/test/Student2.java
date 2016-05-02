package com.jfcheng.json.test;

import com.jfcheng.json.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by jfcheng on 4/30/16.
 */
public class Student2 {
    @Required
    @JsonName("id")
    @MinValue(1l)
    Long stuId;

    String name;

    @MinValue(0)
    @MaxValue(200)
    short age;

    @RangeValue(min = 1, max = 350)
    short height;

    @ValidNumbers({1, 3, 5})
    int classLevel;

    @ValidLength(min = 2, max = 5)
    String[] courses;

    @ValidLength(min = 2, max = 5)
    List<Integer> scores;

    @ValidLength(min = 3, max = 15)
    String englishName;

    @ValidLength(min = 2, max = 5)
    Map<String, Integer> scoreMap;

    @StringRegex(value = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", name = "Date(YYYY-MM-DD)")
    String birthday;

    @ValidStrings({"Male", "Female"})
    String gender;

    @JsonToEntityIgnore
    @EntityToJsonIgnore
    String nonSense;
}
