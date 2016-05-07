package com.jfcheng.json.test;

import com.jfcheng.json.annotation.exception.ValidationFailException;
import com.jfcheng.json.parse.JsonObject;
import com.jfcheng.json.parse.JsonParser;
import com.jfcheng.json.parse.JsonUtils;
import com.jfcheng.json.parse.exception.JsonValueParseException;
import com.jfcheng.json.annotation.exception.InvalidParameterValueException;
import com.jfcheng.json.annotation.exception.RequiredFieldNotFoundException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by jfcheng on 4/30/16.
 */
public class JsonUtilsValidationTest {

    private static String RESOURCE_ROOT = "/Users/jfcheng/IdeaProjects/ObjectValidations/JsonObjectValidations/src/main/resources/json-parser/annotation/";

    @Test
    public void testRequiredAndJsonNameAnnotation() throws ValidationFailException, InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-Required.json");
        boolean isFail = false;
        try {
            JsonUtils.jsonTextToEntityWithValidation(reader, Student2.class);
        } catch (RequiredFieldNotFoundException e) {
            System.out.println(e);
            isFail = true;
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testMinValueAnnotation() throws  JsonValueParseException, ClassNotFoundException, InstantiationException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-MinValue.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testMaxValueAnnotation() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-MaxValue.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testRangeValueAnnotation() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-RangeValue.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidNumbersAnnotation() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidNumbers.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidLengthAnnotationForArray() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidLength.json");
        doInvalidParameterTest(reader);
    }


    @Test
    public void testValidLengthAnnotationForCollection() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidLengthForCollection.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidLengthAnnotationForString() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidLengthForString.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidLengthAnnotationForMap() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidLengthForMap.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testStringRegexAnnotation() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-StringRegex.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidStringsAnnotation() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidStrings.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidStringsAnnotationWithString() throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        String str = FileReader.readToString(RESOURCE_ROOT + "student2-ValidStrings.json");
        boolean isFail = false;
        try {
            JsonUtils.jsonStringToEntityWithValidation(str, Student2.class);
        } catch (ValidationFailException e) {
            System.out.println(e);
            isFail = true;
        }
        Assert.assertTrue(isFail);
    }


    @Test
    public void testJsonToEntityIgnoreAnnotation() throws ValidationFailException, InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-JsonToEntityIgnore.json");
        Student2 stu = (Student2) JsonUtils.jsonTextToEntityWithValidation(reader, Student2.class);
        Assert.assertNull(stu.nonSense);
    }


    @Test
    public void testEntityToJsonIgnoreAnnotation() throws InstantiationException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-JsonToEntityIgnore.json");
        Student2 stu = (Student2) JsonUtils.jsonTextToEntity(reader, Student2.class);
        Assert.assertNull(stu.nonSense);
        stu.nonSense = "abc";
        Assert.assertNotNull(stu.nonSense);
        JsonObject jsonValue = (JsonObject) JsonParser.toJsonValue(stu);
        System.out.println(jsonValue);
        Assert.assertNull(jsonValue.get("nonSense"));
    }

    @Test
    public void testEntityToJsonIgnoreAnnotationWithString() throws InstantiationException, JsonValueParseException, ClassNotFoundException {
        String jsonString =FileReader.readToString(RESOURCE_ROOT + "student2-JsonToEntityIgnore.json");
        Student2 stu = (Student2) JsonUtils.jsonStringToEntity(jsonString, Student2.class);
        Assert.assertNull(stu.nonSense);
        stu.nonSense = "abc";
        Assert.assertNotNull(stu.nonSense);
        JsonObject jsonValue = (JsonObject) JsonParser.toJsonValue(stu);
        System.out.println(jsonValue);
        Assert.assertNull(jsonValue.get("nonSense"));
    }



    private void doInvalidParameterTest(Reader reader) throws  InstantiationException, JsonValueParseException, ClassNotFoundException {
        boolean isFail = false;
        try {
            JsonUtils.jsonTextToEntityWithValidation(reader, Student2.class);
        } catch (ValidationFailException e) {
            System.out.println(e);
            isFail = true;
        }
        Assert.assertTrue(isFail);
    }

}
