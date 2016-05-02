package com.jfcheng.json.annotation.exception;

/**
 * Created by jfcheng on 2/27/16.
 */
public class RequiredFieldNotFoundException extends ValidationFailException {
    public RequiredFieldNotFoundException(String message) {
        super(message);
    }

    public RequiredFieldNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
