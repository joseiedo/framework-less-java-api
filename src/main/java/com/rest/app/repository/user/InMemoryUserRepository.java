package com.rest.app.repository.user;

import com.rest.app.domain.user.NewUser;
import com.rest.app.domain.user.User;
import com.rest.app.domain.user.UserRepository;
import com.rest.app.utils.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserRepository implements UserRepository {

    private static final Map USERS_STORE = new ConcurrentHashMap();

    @Override
    public String create(NewUser newUser) {
        String id = UUID.randomUUID().toString();
        User user = User.builder()
                .id(id)
                .login(newUser.getLogin())
                .password(newUser.getPassword())
                .build();
        USERS_STORE.put(newUser.getLogin(), user);

        return id;
    }
}
