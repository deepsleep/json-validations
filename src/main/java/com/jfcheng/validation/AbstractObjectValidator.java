package com.jfcheng.validation;

import com.jfcheng.validation.exception.ValidationFailException;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by jfcheng on 2/27/16.
 */
public abstract class AbstractObjectValidator {

    public  Object doValidation(Object value) throws ValidationFailException{
        return doValueValidation(value, null);
    }

    public Object doValueValidation(Object value, Field field) throws ValidationFailException {
        if(value == null){
            return doNullValidation(value,field);
        }else{
            if(value instanceof Number){
                return  doNumberValidation(value, field);
            }else if(value instanceof Boolean){
                return doBooleanValidation(value, field);
            }else if(value instanceof  Character){
                return doCharacterValidation(value, field);
            }else if(value instanceof String){
                return  doStringValidation(value, field);
            }else if(value.getClass().isArray()){
                return  doArrayValidation( value, field);
            }else if(value instanceof List){
                return  doListValidation(value,field);
            }else if(value instanceof Set){
                return  doSetValidation(value,field);
            }else if(value instanceof Map){
                return  doMapValidation(value,field);
            }else if(value instanceof Enum){
                return  doEnumValidation(value,field);
            }else{
                return  doOtherObjectValidation(value,field);
            }
        }
    }




    protected abstract Object doNullValidation(Object value, Field field) throws ValidationFailException ;

    protected abstract Object doBooleanValidation(Object value, Field field) throws ValidationFailException;

    protected abstract Number doNumberValidation(Object value, Field field) throws ValidationFailException ;

    protected abstract Object doCharacterValidation(Object value, Field field) throws ValidationFailException;

    protected abstract String doStringValidation(Object value, Field field) throws ValidationFailException;

    protected abstract Object doEnumValidation(Object value, Field field) throws ValidationFailException;

    protected abstract Object doArrayValidation(Object value, Field field) throws ValidationFailException;

    protected abstract Object doListValidation(Object value, Field field) throws ValidationFailException;

    protected abstract Object doSetValidation(Object value, Field field) throws ValidationFailException;

    protected abstract Object doMapValidation(Object value, Field field) throws ValidationFailException;

    protected abstract Object doOtherObjectValidation(Object value, Field field) throws ValidationFailException;

}
