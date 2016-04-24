package com.jfcheng.json.parse;

import com.jfcheng.json.parse.exception.JsonValueParseExeception;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by jfcheng on 4/20/16.
 */
public class JsonParserTest {
private static String RESOURCE_ROOT = "/Users/jfcheng/IdeaProjects/ObjectValidations/JsonObjectValidations/src/main/resources/json-parser/";

    @Test
    public void testNullNumberParse(){
        Assert.assertFalse(JsonNoQuotationValue.isNumberValue(null));
    }

    @Test
    public void testReaderIsNull() throws IOException, JsonValueParseExeception {
        Object value = JsonParser.parse(null);
        Assert.assertNull(value);
    }

    @Test
    public void testCorrectJsonObject() throws IOException, JsonValueParseExeception {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"JsonFile0.json");
        JsonObject jsonObject = (JsonObject) JsonParser.parse(reader);
        System.out.println("End of parsing testCorrectJson: " + jsonObject);
        System.out.println(jsonObject.getValues().get("unicode"));
    }

    @Test
    public void testOnlyArrayJson() throws IOException, JsonValueParseExeception {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"JsonOnlyArray.json");
        Object jsonObject = JsonParser.parse(reader);
        System.out.println("End of parsing testOnlyArrayJson: " + jsonObject);
    }

    @Test
    public void testOnlyBooleanJson() throws IOException, JsonValueParseExeception {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"JsonOnlyBoolean.json");
        Object jsonObject = JsonParser.parse(reader);
        System.out.println("End of parsing testOnlyBooleanJson : " + jsonObject);
    }

    @Test
    public void testOnlyNullJson() throws IOException, JsonValueParseExeception {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"JsonOnlyNull.json");
        Object jsonObject = JsonParser.parse(reader);
        System.out.println("End of parsing testOnlyNullJson: " + jsonObject);
    }

    @Test
    public void testOnlyNumberJson() throws IOException, JsonValueParseExeception {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"JsonOnlyNumber.json");
        Object jsonObject = JsonParser.parse(reader);
        System.out.println("End of parsing testOnlyNumberJson: " + jsonObject);
    }


    @Test
    public void testSelfDefineFail1() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"JsonFailTest1.json");
        Object jsonObject;
        boolean isFail = false;
        try {
             jsonObject = JsonParser.parse(reader);
        }catch(JsonValueParseExeception e){
            e.printStackTrace();
            isFail =true;
        }finally {
            reader.close();
        }
        Assert.assertTrue(isFail);

    }

    @Test
    public void testJsonOrgSuitPass1() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/pass1.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertFalse(isFail);
    }

    @Test
    public void testJsonOrgSuitPass2() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/pass2.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertFalse(isFail);
    }

    @Test
    public void testJsonOrgSuitPass3() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/pass3.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertFalse(isFail);
    }

    @Test
    public void testJsonOrgSuitFail1() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail1.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertFalse(isFail); // This sample data actually is correct
    }

    @Test
    public void testJsonOrgSuitFail2() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail2.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail3() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail3.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail4() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail4.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail5() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail5.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail6() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail6.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail7() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail7.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail8() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail8.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail9() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail9.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail10() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail10.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail11() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail11.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail12() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail12.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail13() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail13.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail14() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail14.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail15() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail15.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail16() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail16.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail17() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail17.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail18() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail18.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertFalse(isFail); // The sample data is correct, not fail.
    }

    @Test
    public void testJsonOrgSuitFail19() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail19.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail20() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail20.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail21() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail21.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail22() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail22.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail23() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail23.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail24() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail24.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail25() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail25.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail26() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail26.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail27() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail27.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail28() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail28.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail29() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail29.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail30() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail30.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail31() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail31.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail32() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail32.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }

    @Test
    public void testJsonOrgSuitFail33() throws IOException {
        Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +"json-org-testsuit/fail33.json");
        Object jsonObject = null;
        boolean isFail = false;
        try {
            jsonObject = JsonParser.parse(reader);
        } catch (JsonValueParseExeception e) {
            e.printStackTrace();
            isFail = true;
        }finally{
            reader.close();
        }
        Assert.assertTrue(isFail);
    }


    // make it not runnable
    public static void mains(String[] args){
        // for 3 pass test case
        String passFilePrefix = "pass";
        for(int i = 1; i< 4; i++){
            printCase(passFilePrefix + i);
            System.out.println();
        }

        // 33 fail cases
        String failFilePrefix = "fail";
        for(int i = 1; i< 34; i++){
            printCase(failFilePrefix + i);
            System.out.println();
        }
    }

    private static void printCase(String fileName){
        String caseName = fileName.substring(0, 1).toUpperCase() + fileName.substring(1);
        String line1 = "@Test";
        String line2_1 = "public void testJsonOrgSuit";
        String line2_2 = caseName; // TestCaseName
        String line2_3 = "() throws IOException {";
        String line3_1 = "Reader reader = FileReader.getBufferReader(RESOURCE_ROOT +\"json-org-testsuit/";
        String line3_2 = fileName; // FileName
        String line3_3 = ".json\");";
        String line4 = "Object jsonObject = null;";
        String line5 = "boolean isFail = false;";
        String line6 = "try {";
        String line7 = "  jsonObject = JsonParser.parse(reader);";
        String line8 = "} catch (JsonValueParseExeception e) {";
        String line9 = " e.printStackTrace();";
        String line10 = "  isFail = true;";
        String line11 = "}finally{";
        String line12 = "reader.close();";
        String line13 = " }";
        String line14 = "Assert.assertTrue(isFail);";
        String line15 = " }";

        System.out.println(line1);
        System.out.println(line2_1 + line2_2+line2_3);
        System.out.println(line3_1 + line3_2 +line3_3);
        System.out.println(line4);
        System.out.println(line5);
        System.out.println(line6);
        System.out.println(line7);
        System.out.println(line8);
        System.out.println(line9);
        System.out.println(line10);
        System.out.println(line11);
        System.out.println(line12);
        System.out.println(line13);
        System.out.println(line14);
        System.out.println(line15);

    }
}
