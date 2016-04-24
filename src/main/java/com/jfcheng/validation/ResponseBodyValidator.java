package com.jfcheng.validation;

import com.jfcheng.validation.annotation.RequestAnnotationHelper;
import com.jfcheng.validation.exception.ValidationFailException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Created by jfcheng on 2/29/16.
 */
public class ResponseBodyValidator extends AbstractObjectValidator {
    private static ResponseBodyValidator validator = new ResponseBodyValidator();

    private ResponseBodyValidator(){
        super();
    }

    public static AbstractObjectValidator getValidator(){
        return validator;
    }

    public static Object doResponseBodyValidation(Object value) throws ValidationFailException {
        return validator.doValueValidation(value, null);
    }


    @Override
    protected Object doNullValidation(Object value, Field field) throws ValidationFailException {
        return null;
    }

    @Override
    protected Object doBooleanValidation(Object value, Field field) {
        return value;
    }

    @Override
    protected Number doNumberValidation(Object value, Field field) throws ValidationFailException {
        return (Number) value;
    }

    @Override
    protected Character doCharacterValidation(Object value, Field field) {
        return (Character) value;
    }

    @Override
    protected String doStringValidation(Object value, Field field) throws ValidationFailException {
        return (String) value;
    }

    @Override
    protected Object doEnumValidation(Object value, Field field) throws ValidationFailException {
        return value;
    }

    @Override
    protected Object doArrayValidation(Object value, Field field) throws ValidationFailException {
        int length = Array.getLength(value);
        System.out.println(value.getClass());
        Object newArray = Array.newInstance(value.getClass(),length);

        for(int i=0; i< length; i++){
            Object elem = Array.get(value,i);
           // System.out.println("ddddddd" +elem.getClass() );
            if(elem instanceof Number ||  elem instanceof Number  || elem instanceof String){
           //     System.out.println("ddddddd");
                newArray  = value;
            }else {
                //Array.newInstance(value.getClass(),length);
               // Constructor ctor = value.getClass().getConstructor(elem.getClass());
               // Array.set(newArray, i, ctor.newInstance(doValueValidation(elem, null));
            }
        }

        return value;
    }

    @Override
    protected Object doListValidation(Object value, Field field) throws ValidationFailException {
        Collection<Object> newList =doCollectionValidation(value,field);
        return newList;
    }

    @Override
    protected Object doSetValidation(Object value, Field field) throws ValidationFailException {
        Collection<Object> newSet = doCollectionValidation(value,field);
        return newSet;

    }

    private Collection<Object> doCollectionValidation( Object value, Field field) throws ValidationFailException {
        Object newObj;
        try {
            newObj = value.getClass().newInstance();
        } catch (InstantiationException |IllegalAccessException e) {
            throw new ValidationFailException(e.getMessage(),e);
        }

        Collection newCollection = (Collection) newObj;
        Iterator iterator = ((Collection) value).iterator();
        while(iterator.hasNext()){
            Object elemVal = iterator.next();
            newCollection.add(doValueValidation(elemVal,null));
        }
        return newCollection;
    }

    @Override
    protected Object doMapValidation(Object value, Field field) throws ValidationFailException {
        Object newObj = null;
        try {
            newObj = value.getClass().newInstance();
        } catch (InstantiationException |IllegalAccessException e) {
            throw new ValidationFailException(e.getMessage(),e);
        }
        Map<Object, Object> newMap = (Map<Object, Object>) newObj;

        Set keys = ((Map) value).keySet();
        if(keys!=null && keys.size() > 0) {
            for (Object key : keys) {
               newMap.put(doValueValidation(key,null), doValueValidation(((Map) value).get(key), null));
            }

        }
        return newMap;
    }

    @Override
    protected Object doOtherObjectValidation(Object value, Field field) throws ValidationFailException {
        if(value == null){
            return null;
        }else{
            Class clazz = value.getClass();

            Object responseObj;
            try {
                responseObj = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ValidationFailException(e.getMessage(), e);
            }

            List<Field> fields = ReflectionUtils.getAllInstanceFields(clazz);
            if (fields != null && fields.size() > 0) {
                for (Field f : fields) {

                    Object fieldValue;
                    f.setAccessible(true);
                    try {
                        fieldValue = f.get(value);
                        if(fieldValue !=null && !RequestAnnotationHelper.isResponseParameterIgnore(fieldValue, f.getAnnotations())){
                            f.set(responseObj, doValueValidation(fieldValue,f));
                        }
                    } catch (IllegalAccessException e) {
                        throw new ValidationFailException(e.getMessage(), e);
                    }


                }
            }
            return responseObj;
        }
    }
}
