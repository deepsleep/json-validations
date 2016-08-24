package com.jfcheng.json.test;

import com.jfcheng.json.parse.JsonParser;
import com.jfcheng.json.parse.JsonUtils;
import com.jfcheng.json.parse.JsonValue;
import com.jfcheng.json.parse.exception.JsonValueParseException;
import com.jfcheng.json.annotation.exception.InvalidParameterValueException;
import com.jfcheng.json.annotation.exception.RequiredFieldNotFoundException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jfcheng on 4/26/16.
 */
//TODO: TO REMOVE THIS CLASS.
public class JsonUtilsTest {
    private static String RESOURCE_ROOT = "/Users/jfcheng/IdeaProjects/ObjectValidations/JsonObjectValidations/src/main/resources/json-parser/";


    @Test
    public void testStudent() throws JsonValueParseException, ClassNotFoundException, InstantiationException, IOException, IllegalAccessException, InvalidParameterValueException, RequiredFieldNotFoundException {
        Reader reader = FileReaderUtils.getBufferReader(RESOURCE_ROOT + "student.json");
        //System.out.println(JsonParser.parse(reader));
        Student stu = (Student) JsonUtils.jsonTextToEntity(reader, Student.class);
        System.out.println("stu.c = " + stu.charValue);
        //System.out.println(stu.getAge());
        //System.out.println(stu.getName());

        //stu.printLuckyNumber();
        JsonValue j1 = JsonParser.toJsonValue(stu);
      //  System.out.println(((JsonObject)j1).get("charValue"));
       // System.out.println(j1.toJsonText());
        System.out.println("-----IgnoreNullField is false--------");
        System.out.println(JsonParser.toJsonValue(stu).toJsonText(false));


    }

    @Test
    public void testEquals() throws IOException, JsonValueParseException {
        Reader r1 = FileReaderUtils.getBufferReader(RESOURCE_ROOT + "student.json");
        JsonValue j1 = JsonParser.parse(r1);

        Reader r2 = FileReaderUtils.getBufferReader(RESOURCE_ROOT + "student.json");
        JsonValue j2 = JsonParser.parse(r2);

        Assert.assertEquals(j1, j2);
    }


    @Test
    public void testOnlyNumberValues() throws JsonValueParseException, ClassNotFoundException, InstantiationException, IOException, IllegalAccessException, InvalidParameterValueException, RequiredFieldNotFoundException {
        String num = "10";
        int i1 = (int) JsonUtils.jsonStringToEntity(num, int.class);
        Integer i2 = (Integer) JsonUtils.jsonStringToEntity(num, Integer.class);
        short i3 = (short) JsonUtils.jsonStringToEntity(num, short.class);
        Short i4 = (Short) JsonUtils.jsonStringToEntity(num, Short.class);
        long i5 = (long) JsonUtils.jsonStringToEntity(num, long.class);
        Long i6 = (Long) JsonUtils.jsonStringToEntity(num, Long.class);
        byte i7 = (byte) JsonUtils.jsonStringToEntity(num, byte.class);
        Byte i8 = (byte) JsonUtils.jsonStringToEntity(num, Byte.class);
        float i9 = (float) JsonUtils.jsonStringToEntity(num, float.class);
        Float i10 = (Float) JsonUtils.jsonStringToEntity(num, Float.class);
        double i11 = (double) JsonUtils.jsonStringToEntity(num, double.class);
        Double i12 = (Double) JsonUtils.jsonStringToEntity(num, Double.class);
        Assert.assertTrue(10 == i1);
        Assert.assertTrue(10 == i2);
        Assert.assertTrue(10 == i3);
        Assert.assertTrue(10 == i4);
        Assert.assertTrue(10 == i5);
        Assert.assertTrue(10 == i6);
        Assert.assertTrue(10 == i7);
        Assert.assertTrue(10 == i8);
        Assert.assertTrue(10.0 == i9);
        Assert.assertTrue(10.0 == i10);
        Assert.assertTrue(10.0 == i11);
        Assert.assertTrue(10.0 == i12);
    }



    @Test
    public void testOnlyString() throws JsonValueParseException, ClassNotFoundException, InstantiationException, IOException, IllegalAccessException, InvalidParameterValueException, RequiredFieldNotFoundException {
        String str = "\"json text string\"";
        String result = (String) JsonUtils.jsonStringToEntity(str, String.class);
        Assert.assertEquals("json text string", result);

        String c = "\"c\"";
        char r2 = (char) JsonUtils.jsonStringToEntity(c, char.class);
        Character r3 = (Character) JsonUtils.jsonStringToEntity(c, Character.class);
        Assert.assertEquals('c', r2);
        Assert.assertEquals(new Character('c'), r3);

        String enumStr = "\"MON\"";
        WeekDataEnum enuValue = (WeekDataEnum) JsonUtils.jsonStringToEntity(enumStr, WeekDataEnum.class);
        Assert.assertEquals(WeekDataEnum.MON, enuValue);
    }

    @Test
    public void testOnlyArray() throws JsonValueParseException, ClassNotFoundException, InstantiationException, IOException, IllegalAccessException, InvalidParameterValueException, RequiredFieldNotFoundException {
        String arr = "[1,2,3,4]";

        // case 1
        int[] expected1 = {1, 2, 3, 4};
        int[] r1 = (int[]) JsonUtils.jsonStringToEntity(arr, int[].class);
        Assert.assertArrayEquals(expected1, r1);

        // case 2
        Integer[] expected2 = {1, 2, 3, 4};
        Integer[] r2 = (Integer[]) JsonUtils.jsonStringToEntity(arr, Integer[].class);
        Assert.assertArrayEquals(expected2, r2);

        String[] expected3 = {"1", "2", "3", "4"};
        String[] r3 = (String[]) JsonUtils.jsonStringToEntity(arr, String[].class);
        Assert.assertArrayEquals(expected3, r3);

        Type[] listParametersTypes = {Integer.class};
        List<Integer> r4 = (List<Integer>) JsonUtils.jsonStringToEntity(arr, List.class, listParametersTypes);
        Assert.assertArrayEquals(expected2, r4.toArray());

        // case3
        Set<Integer> r5 = (Set<Integer>) JsonUtils.jsonStringToEntity(arr, Set.class, listParametersTypes);
        Assert.assertArrayEquals(expected2, r5.toArray());
    }

    @Test
    public void testDuplicatedValueInSet() throws ClassNotFoundException, InstantiationException, IOException, IllegalAccessException, InvalidParameterValueException, RequiredFieldNotFoundException {
        String arr = "[1,2,3,2]";
        Type[] types = {Integer.class};
        boolean duplicated = false;
        try {
            JsonUtils.jsonStringToEntity(arr, Set.class, types);
        } catch (JsonValueParseException e) {
            if(e.getMessage().contains("duplicated")){
                duplicated = true;
            }
        }
        Assert.assertTrue(duplicated);

    }

    @Test
    public void testOnlyMap() throws JsonValueParseException, ClassNotFoundException, InstantiationException, IOException, IllegalAccessException, InvalidParameterValueException, RequiredFieldNotFoundException {
        String str1 = "{\"1\": 10, \"2\": 20}";
        Type[] mapParametersTypes = {String.class, Integer.class};
        Map r1 = (Map) JsonUtils.jsonStringToEntity(str1, Map.class, mapParametersTypes);
//        System.out.println(r1);
//        System.out.println(r1.get("1"));
//        System.out.println(r1.get("2"));

        Type[] mapParametersTypes2 = {Integer.class, Integer.class};
        Map r2 = (Map) JsonUtils.jsonStringToEntity(str1, Map.class, mapParametersTypes2);
//        System.out.println(r2);
//        System.out.println(r2.get(1));
//        System.out.println(r2.get(2));
        Assert.assertEquals(r1.get("1"), r2.get(1));
        Assert.assertEquals(r1.get("2"), r2.get(2));
    }


}
