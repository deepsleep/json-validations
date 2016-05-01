package com.jfcheng.validation;

import com.jfcheng.utils.ReflectionUtils;
import com.jfcheng.validation.annotation.AnnotationHelper;
import com.jfcheng.validation.exception.InvalidParameterValueException;
import com.jfcheng.validation.exception.RequiredFieldNotFoundException;
import com.jfcheng.validation.exception.ValidationFailException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by jfcheng on 2/27/16.
 */
public class RequestBodyValidator extends AbstractObjectValidator {
    private static RequestBodyValidator validator = new RequestBodyValidator();

    private RequestBodyValidator() {
        super();
    }

    public static RequestBodyValidator getValidator() {
        return validator;
    }

    public static Object doRequestBodyValidation(Object value) throws ValidationFailException {
        return validator.doValueValidation(value, null);
    }


    @Override
    protected Object doNullValidation(Object value, Field field) throws ValidationFailException {
        AnnotationHelper.doRequiredAnnotationValidation(field.getName(), value, field.getAnnotations());
        return null;
    }

    @Override
    protected Object doBooleanValidation(Object value, Field field) {
        return value;
    }

    @Override
    protected Number doNumberValidation(Object value, Field field) throws ValidationFailException {
        AnnotationHelper.doNumberAnnotationValidation(field.getName(),value, field.getAnnotations());
        return (Number) value;
    }

    @Override
    protected Object doCharacterValidation(Object value, Field field) {
        return value;
    }

    @Override
    protected String doStringValidation(Object value, Field field) throws ValidationFailException {
        AnnotationHelper.doStringAnnotationValidation(field.getName(), value, field.getAnnotations());
        return (String) value;
    }

    @Override
    protected Object doEnumValidation(Object value, Field field) throws ValidationFailException {
        return value;
    }

    @Override
    protected Object doArrayValidation(Object value, Field field) throws ValidationFailException {
        AnnotationHelper.doCollectionAnnotationValidation(field.getName(), value, field.getAnnotations());
        int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            doValueValidation(Array.get(value, i), null);
        }
        return value;
    }

    @Override
    protected Object doListValidation(Object value, Field field) throws ValidationFailException {
        AnnotationHelper.doCollectionAnnotationValidation(field.getName(), value, field.getAnnotations());
        doCollectionValidation(value, field);
        return value;
    }

    @Override
    protected Object doSetValidation(Object value, Field field) throws ValidationFailException {
        AnnotationHelper.doCollectionAnnotationValidation(field.getName(), value, field.getAnnotations());
        doCollectionValidation(value, field);
        return value;
    }

    @Override
    protected Object doMapValidation(Object value, Field field) throws ValidationFailException {
        Set<?> keys = ((Map<?, ?>) value).keySet();
        if (keys != null) {
            for (Object key : keys) {
                doValueValidation(key, field);
                doValueValidation(((Map<?, ?>) value).get(key), field);
            }
        }
        return value;
    }

    @Override
    protected Object doOtherObjectValidation(Object value, Field field) throws ValidationFailException {
        if (value == null) {
            return null;
        } else {
            Class clazz = value.getClass();
            List<Field> fields = ReflectionUtils.getAllInstanceFields(clazz);
            if (fields != null && fields.size() > 0) {
                for (Field f : fields) {

                    String fieldName = f.getName();
                    Object fieldValue;
                    f.setAccessible(true);
                    try {
                        fieldValue = f.get(value);
                    } catch (IllegalAccessException e) {
                        throw new ValidationFailException(e.getMessage(), e);
                    }

                    try {
                        doValueValidation(fieldValue, f);
                    } catch (InvalidParameterValueException e1) {
                        throw new InvalidParameterValueException("Invalid parameter value in [ " + fieldName + " ]: " + e1.getMessage());
                    } catch (RequiredFieldNotFoundException e2) {
                        throw new RequiredFieldNotFoundException("Parameter [ " + fieldName + " ] is required.");
                    }
                }
            }
            return value;
        }
    }

    private void doCollectionValidation(Object value, Field field) throws ValidationFailException {
        Iterator<?> iterator = ((Collection<?>) value).iterator();
        while (iterator.hasNext()) {
            doValueValidation(iterator.next(), field);
        }
    }

}
