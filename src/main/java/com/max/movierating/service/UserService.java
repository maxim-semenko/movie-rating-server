package com.max.movierating.service;

import com.max.movierating.entity.User;

public interface UserService {

    User getByUsername(String username);

}