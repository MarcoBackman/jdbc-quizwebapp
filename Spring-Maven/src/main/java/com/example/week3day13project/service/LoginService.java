package com.example.week3day13project.service;

import com.example.week3day13project.dao.implementation.UserDAO;
import com.example.week3day13project.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final UserDAO userDao;

    @Autowired
    public LoginService(UserDAO userDao) {this.userDao = userDao; }

    public Optional<User> validateLogin(String username, String password) {
        return userDao.validateLogin(username, password);
    }
}
