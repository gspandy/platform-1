package com.hundsun.fcloud.servlet.caller;

/**
 * Created by Gavin Hu on 2015/1/23.
 */
public class ServletCallerException extends RuntimeException {

    public ServletCallerException(String message) {
        super(message);
    }

    public ServletCallerException(String message, Throwable cause) {
        super(message, cause);
    }
}
