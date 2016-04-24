package com.jfcheng.validation.exception;

/**
 * Created by jfcheng on 2/28/16.
 */
public class InvalidParameterValueException extends ValidationFailException {
    public InvalidParameterValueException(String message) {
        super(message);
    }

    public InvalidParameterValueException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
