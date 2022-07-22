package com.example.week3day13project.controller;

import com.example.week3day13project.Week3Day13ProjectApplication;
import com.example.week3day13project.domain.User;
import com.example.week3day13project.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(value = "/login")
    public String showLoginPage(Model model) {
        return "loginPage";
    }

    @PostMapping("/login")
    public String postLogin(@RequestParam("user_id") String username,
                            @RequestParam("password") String password,
                            HttpServletRequest request, Model model) {

        Optional<User> possibleUser = loginService.validateLogin(username, password);


        if(possibleUser.isPresent()) {

            User user = possibleUser.get();

            //Check if the user has been suspended
            if(user.isSuspended()) {
                return "loginPage";
            }

            //Add user as active user once logged in
            Week3Day13ProjectApplication.activeUsers.put(user.getUserId(), user);

            HttpSession oldSession = request.getSession(false);
            // invalidate old session if it exists
            if (oldSession != null) oldSession.invalidate();

            // generate new session
            HttpSession newSession = request.getSession(true);

            // store username and email in session
            newSession.setAttribute("userObject", possibleUser.get());

            return "redirect:home";
        } else {
            return "loginPage";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {

        HttpSession oldSession = request.getSession(false);
        User user = (User)oldSession.getAttribute("userObject");

        //Remove from active user
        Week3Day13ProjectApplication.activeUsers.remove(user.getUserId());

        // invalidate old session if it exists
        if(oldSession != null) oldSession.invalidate();
        return "loginPage";
    }
}
