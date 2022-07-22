package com.example.week3day13project.controller;

import com.example.week3day13project.domain.User;
import com.example.week3day13project.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Controller
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController (FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @RequestMapping(value = "/feedback")
    public String getFeedbackPage(HttpServletRequest request) {
        HttpSession currentSession = request.getSession(false);

        if (currentSession == null) { //Expired or no session
            return "loginPage";
        } else {
            User user = (User)currentSession.getAttribute("userObject");
            String userID = String.valueOf(user.getUserId());
            //check if user has already gave a feedback
            System.out.println("Feedback requested");
            if (feedbackService.allowedToFeedback(userID)) {
                return "feedbackPage";
            } else { //if not, redirect to the page
                System.out.println("User has already rated");
                return "mainPage";
            }
        }
    }

    @PostMapping("/feedback")
    public String insertFeedback(@RequestParam("rating") String rate,
                                 @RequestParam("feedback") String content,
                                 HttpServletRequest request) {
        HttpSession currentSession = request.getSession(false);
        if (currentSession == null) { //Expired or no session
            return "loginPage";
        } else {
            User user = (User) currentSession.getAttribute("userObject");
            String userID = Integer.toString(user.getUserId());
            feedbackService.leaveFeedBack(rate, content);
            feedbackService.updateUserFeedbackStatus(userID);
        }
        return "redirect:mainPage";
    }
}
