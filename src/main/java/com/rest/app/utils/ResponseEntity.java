package com.rest.app.utils;

import com.sun.net.httpserver.Headers;
import lombok.Value;

@Value
public class ResponseEntity<T> {

    private final T body;
    private final Headers headers;
    private final HttpStatusCode httpStatusCode;
}