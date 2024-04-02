package com.rest.app.controller.user.register;

import com.rest.app.domain.user.User;

import java.util.List;

record ListUsersResponse(List<User> users) {

}