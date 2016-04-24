package com.jfcheng.validations.test.entity;

import com.jfcheng.validation.ObjectToJsonStringValidator;
import com.jfcheng.validation.RequestBodyValidator;
import com.jfcheng.validation.ResponseBodyValidator;
import com.jfcheng.validation.exception.ValidationFailException;

/**
 * Created by jfcheng on 2/27/16.
 */
public class TestMain {


    public static void main(String[] args) throws ValidationFailException {
        TestDerivedClass derivedObject = new TestDerivedClass(11,"123","DERIVED",new OtherClass("Other"),10);


        long startTime = System.currentTimeMillis();
        String s1 = (String)ObjectToJsonStringValidator.objectToJsonStringValidation(derivedObject);
        long endTime =System.currentTimeMillis();


//        Gson gson = new Gson();
//        long startTime2 = System.currentTimeMillis();
//        String s2 = gson.toJson(derivedObject);
//        long endTime2 = System.currentTimeMillis();

        System.out.println(s1);
        System.out.println("--------------------------");
//        System.out.println(s2);
        System.out.println("My objectToJson time 1:  " + (endTime -startTime));
//        System.out.println("Gso toJson time 2:  " + (endTime2 -startTime2));
//        System.out.println(gson.toJson(derivedObject));
        Object response = ResponseBodyValidator.doResponseBodyValidation(derivedObject);
        System.out.println((String)ObjectToJsonStringValidator.objectToJsonStringValidation(response) );

        try {
            System.out.println(RequestBodyValidator.doRequestBodyValidation(derivedObject));
        } catch (ValidationFailException e) {
            System.out.println(e.getMessage());
        }
//        System.out.println("-------values----------");
//        System.out.println(newObject.getDerivedStr());
//        System.out.println(newObject.getOther().getOtherStr());
//        System.out.println(newObject.getBaseInt());
//        System.out.println(newObject.getBaseStr());
    }

}
