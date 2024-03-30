package com.rest.app.domain.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {

    String id;
    String login;
    String password;
}
