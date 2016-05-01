package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseException;
import com.jfcheng.validation.exception.InvalidParameterValueException;
import com.jfcheng.validation.exception.RequiredFieldNotFoundException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by jfcheng on 4/30/16.
 */
public class JsonValidationTest {

    private static String RESOURCE_ROOT = "/Users/jfcheng/IdeaProjects/ObjectValidations/JsonObjectValidations/src/main/resources/json-parser/annotation/";

    @Test
    public void testRequiredAndJsonNameAnnotation() throws IllegalAccessException, InvalidParameterValueException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-Required.json");
        boolean isFail =false;
        try {
            JsonUtils.jsonTextToEntityWithValidation(reader, Student2.class);
        }catch(RequiredFieldNotFoundException e){
            System.out.println(e);
            isFail = true;
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testMinValueAnnotation() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {

        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-MinValue.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testMaxValueAnnotation() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {

        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-MaxValue.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testRangeValueAnnotation() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-RangeValue.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidNumbersAnnotation() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidNumbers.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidLengthAnnotationForArray() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidLength.json");
        doInvalidParameterTest(reader);
    }


    @Test
    public void testValidLengthAnnotationForCollection() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidLengthForCollection.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidLengthAnnotationForString() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidLengthForString.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testValidLengthAnnotationForMap() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidLengthForMap.json");
        doInvalidParameterTest(reader);
    }

    @Test
    public void testStringRegexAnnotation() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-StringRegex.json");
        doInvalidParameterTest(reader);
    }

 @Test
    public void testValidStringsAnnotation() throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT + "student2-ValidStrings.json");
        doInvalidParameterTest(reader);
    }


    private void doInvalidParameterTest(Reader reader) throws IllegalAccessException, RequiredFieldNotFoundException, InstantiationException, IOException, JsonValueParseException, ClassNotFoundException {
        boolean isFail =false;
        try {
            JsonUtils.jsonTextToEntityWithValidation(reader, Student2.class);
        } catch (InvalidParameterValueException e) {
            System.out.println(e);
            isFail = true;
        }
        Assert.assertTrue(isFail);
    }

}
