package com.rest.app.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    ACCEPTED(202),

    BAD_REQUEST(400),
    METHOD_NOT_ALLOWED(405);

    private int code;
}