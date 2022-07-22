package com.example.week3day13project;

import com.example.week3day13project.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.ArrayList;
import java.util.Hashtable;

@SpringBootApplication
@ServletComponentScan
public class Week3Day13ProjectApplication {

    //Logged-in users list - Make sure to remove user on session expiration
    public static Hashtable<Integer, User> activeUsers = new Hashtable<>();

    public static void main(String[] args) {
        SpringApplication.run(Week3Day13ProjectApplication.class, args);
    }

}
