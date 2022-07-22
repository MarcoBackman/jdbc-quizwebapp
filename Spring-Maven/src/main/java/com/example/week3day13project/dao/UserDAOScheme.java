package com.example.week3day13project.dao;

import com.example.week3day13project.domain.User;

import java.util.List;

public interface UserDAOScheme {
    List<User> findAll();
    List<User> findById(String userKey);
    List<User> findByName(String name);
    boolean insertUser(String userName,
                       String userPW,
                       String email,
                       String firstName,
                       String lastName);

    boolean updateUser(String userKey,
                       String userPW,
                       String email);

    boolean deleteUser(String userKey);
    boolean suspendUser(String userKey);
    boolean unSuspendUser(String userKey);
    boolean grantAdmin(String userKey); //This is only for a test version
    boolean setHasRated(String userID);
}
