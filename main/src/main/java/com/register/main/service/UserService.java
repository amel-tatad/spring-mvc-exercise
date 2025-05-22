package com.register.main.service;

import com.register.main.model.User;
import java.util.List;

public interface UserService {
    User findById(Long userId);

    User save(User user);

    void deleteById(Long userId);

    List<User> findAll();
}