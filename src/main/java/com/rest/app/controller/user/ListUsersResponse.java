package com.rest.app.controller.user;

import com.rest.app.domain.user.User;

import java.util.List;

record ListUsersResponse(List<User> users) {

}