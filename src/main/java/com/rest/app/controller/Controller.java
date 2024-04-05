package com.rest.app.controller;

import com.rest.app.config.ObjectMapperWrapper;
import com.rest.app.config.exceptions.ApplicationExceptions;
import com.rest.app.config.exceptions.handler.GlobalExceptionHandler;
import com.rest.app.utils.Inject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.vavr.control.Try;

import java.io.InputStream;

public abstract class Controller {

    @Inject
    public ObjectMapperWrapper objectMapperWrapper;

    @Inject
    public GlobalExceptionHandler exceptionHandler;


    public void handle(HttpExchange exchange) {
        Try.run(() -> execute(exchange))
                .onFailure(thr -> exceptionHandler.handle(thr, exchange));
    }

    protected abstract void execute(HttpExchange exchange) throws Exception;


    protected <T> T readRequest(InputStream is, Class<T> type) {
        return Try.of(() -> objectMapperWrapper.getObjectMapper().readValue(is, type))
                .getOrElseThrow(ApplicationExceptions.invalidRequest());
    }

    protected <T> byte[] writeResponse(T response) {
        return Try.of(() -> objectMapperWrapper.getObjectMapper().writeValueAsBytes(response))
                .getOrElseThrow(ApplicationExceptions.invalidRequest());
    }

    protected static Headers getHeaders(String key, String value) {
        Headers headers = new Headers();
        headers.set(key, value);
        return headers;
    }
}
