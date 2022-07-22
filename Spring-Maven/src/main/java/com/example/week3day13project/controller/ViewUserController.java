package com.example.week3day13project.controller;

import com.example.week3day13project.domain.User;
import com.example.week3day13project.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class ViewUserController {

    private final UserInfoService userInfoService;
    private User targetUser;

    public ViewUserController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping(value = "/user-profile")
    public String viewUserInfo(HttpServletRequest request, Model model) {
        String userId = request.getParameter("user_info");
        Optional<User> user = userInfoService.getUserById(userId);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            this.targetUser = user.get();
        }
        return "userProfile";
    }

    @PostMapping("/activate_user")
    public String activateUser() {
        if (targetUser != null) {
            boolean result = userInfoService.activateUser(String.valueOf(targetUser.getUserId()));
            if (result) {
                targetUser.setSuspended(false);
            }
        }
        return "redirect:/admin-page";
    }

    @PostMapping("/suspend_user")
    public String suspendUser() {
        if (targetUser != null) {
            boolean result = userInfoService.suspendUser(String.valueOf(targetUser.getUserId()));
            if (result) {
                targetUser.setSuspended(true);
            }
        }
        return "redirect:/admin-page";
    }
}
