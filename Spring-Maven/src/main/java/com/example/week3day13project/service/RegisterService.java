package com.example.week3day13project.service;

import com.example.week3day13project.dao.implementation.UserDAO;
import com.example.week3day13project.domain.User;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterService {
    private final UserDAO userDao;

    @Autowired
    public RegisterService(UserDAO userDao) {this.userDao = userDao; }

    public int validateRegister(String userName, String userEmail) {
        Optional<User> resultByName = userDao.getByUserName(userName);
        Optional<User> resultByEmail = userDao.getByEmail(userEmail);
        if (resultByEmail.isPresent()) {
            return 1;
        } else if (resultByName.isPresent()) {
            return 2;
        } else {
            return 0;
        }
    }

    @Builder
    public boolean registerUser(String userName,
                            String userPW,
                            String userEmail,
                            String userFirstName,
                            String userLastName) {
        return userDao.insertUser(userName, userPW, userEmail, userFirstName, userLastName);
    }
}
