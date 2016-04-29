package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by jfcheng on 4/29/16.
 */
public class JsonObjectTest {

    @Test
    public void testPutAndGet() throws JsonValueParseException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        final String numKey = "numberValue";
        JsonObject jsonObj = new JsonObject();
        jsonObj.put(numKey, 1);

        Object[] array = {1, 2,"abc"};
        jsonObj.put("arrayValue", array);

        Map<String, Integer> mapValue = new HashMap<String, Integer>();
        jsonObj.put("mapValue", mapValue);

        Assert.assertEquals(1, jsonObj.getByte(numKey));
        Assert.assertEquals(1, jsonObj.getShort(numKey));
        Assert.assertEquals(1, jsonObj.getInt(numKey));
        Assert.assertEquals(1, jsonObj.getLong(numKey));

        Assert.assertEquals(1, jsonObj.getFloat(numKey),0.1);
        Assert.assertEquals(1, jsonObj.getDouble(numKey),0.1);

        jsonObj.put("stringValue", "abc");
        Assert.assertEquals(jsonObj.getString("stringValue"), "abc");

        jsonObj.put("booleanValue",true);
        Assert.assertEquals(true, jsonObj.getBoolean("booleanValue"));

        JsonObject obj = jsonObj.getJsonObject("mapValue");
        Assert.assertEquals("{}", obj.toJsonText());



        List<Object> list = (List<Object>) jsonObj.getCollection("arrayValue", List.class, Object.class);
        Assert.assertArrayEquals(array,list.toArray());

        Map<String,Integer> map =jsonObj.getMap("mapValue", Map.class, String.class, Integer.class);

        Student stu = new Student("xxx");
        jsonObj.put("stu", stu);
        Assert.assertEquals("xxx",jsonObj.getJsonObject("stu").get("name"));

        Set<String> keys = jsonObj.keySet();
        String[] expectedKeys = {"numberValue", "mapValue","arrayValue","stu","stringValue","booleanValue"};
        List<String> eKeys = Arrays.asList(expectedKeys);
        Assert.assertTrue(keys.containsAll(eKeys));

        Assert.assertTrue(jsonObj.size() == 6);
        Assert.assertTrue(!jsonObj.isEmpty());
        Assert.assertTrue(jsonObj.containsKey("stu"));

        Assert.assertFalse(jsonObj.putIfAbsent("stu","???"));

        String stuNewVal = "newValue";
        jsonObj.put("stu", stuNewVal);
        Assert.assertEquals(stuNewVal, jsonObj.getString("stu"));
    }

    @Test
    public void testNewStringConstructor() throws JsonValueParseException {
        String s = "{\"name\":\"xiaoming\", \"age\": 12}";
        JsonObject jsonObject = new JsonObject(s);
        Assert.assertEquals("xiaoming", jsonObject.get("name"));
        Assert.assertEquals(12, jsonObject.getInt("age"));
    }
}
