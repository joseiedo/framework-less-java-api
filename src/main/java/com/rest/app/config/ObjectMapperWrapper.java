package com.rest.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.app.utils.Component;

import java.util.Objects;

@Component
public class ObjectMapperWrapper {
    private ObjectMapper objectMapper = null;

    public ObjectMapper getObjectMapper() {
        return Objects.requireNonNullElseGet(objectMapper, ObjectMapper::new);
    }

}
