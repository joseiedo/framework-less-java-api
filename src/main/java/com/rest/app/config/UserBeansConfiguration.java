package com.rest.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.app.domain.user.UserRepository;
import com.rest.app.repository.user.InMemoryUserRepository;

public class UserBeansConfiguration {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final UserRepository USER_REPOSITORY = new InMemoryUserRepository();
    //    private static final UserServiceImpl USER_SERVICE = new UserServiceImpl(USER_REPOSITORY);
//    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

//    public static UserServiceImpl getUserService() {
//        return USER_SERVICE;
//    }

    static UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

//    public static GlobalExceptionHandler getErrorHandler() {
//        return GLOBAL_ERROR_HANDLER;
//    }
}
