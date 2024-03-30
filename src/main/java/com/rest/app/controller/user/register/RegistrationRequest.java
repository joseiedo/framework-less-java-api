package com.rest.app.controller.user.register;

import lombok.Value;

@Value
class RegistrationRequest {

    String login;
    String password;
}
