package com.jfcheng.json.annotation.exception;

/**
 * Created by jfcheng on 2/27/16.
 */
public class ValidationFailException extends Exception {

    public ValidationFailException(String message) {
        super(message);
    }

    public ValidationFailException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
