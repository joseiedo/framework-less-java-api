package com.rest.app.config.exceptions;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final int code;

    ApplicationException(int code, String message) {
        super(message);
        this.code = code;
    }
}