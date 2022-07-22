package com.example.week3day13project.service;

import com.example.week3day13project.dao.implementation.UserDAO;
import com.example.week3day13project.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {
    private final UserDAO userDao;

    @Autowired
    public UserInfoService(UserDAO userDao) {this.userDao = userDao; }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public Optional<User> getUserById(String userId) {
        return userDao.findById(userId).stream().findFirst();
    }

    public boolean suspendUser(String userId) {
        return userDao.suspendUser(userId);
    }

    public boolean activateUser(String userId) {
        return userDao.unSuspendUser(userId);
    }
}
