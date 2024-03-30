package com.rest.app.config.exceptions.handler;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {

    int code;
    String message;
}
