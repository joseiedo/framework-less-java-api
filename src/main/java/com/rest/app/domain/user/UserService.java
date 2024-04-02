package com.rest.app.domain.user;

import java.util.List;

public interface UserService {

    String create(NewUser user);

    List<User> list();
}