package com.rest.app.domain.user;

import com.rest.app.utils.Component;
import com.rest.app.utils.Inject;
import com.rest.app.utils.Qualifier;

@Component
public class UserServiceImpl implements UserService {

    @Inject
    @Qualifier(value = "InMemoryUserRepository")
    public UserRepository userRepository;


    public String create(NewUser user) {
        return userRepository.create(user);
    }

}