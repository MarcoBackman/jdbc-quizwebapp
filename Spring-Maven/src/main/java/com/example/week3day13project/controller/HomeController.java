package com.example.week3day13project.controller;

import com.example.week3day13project.domain.QuizType;
import com.example.week3day13project.domain.User;
import com.example.week3day13project.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
@Controller
public class HomeController {

    private final QuizService quizService;

    public HomeController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping(value = "/home")
    public String showHomePage(Model model, HttpServletRequest request) {
        HttpSession currentSession = request.getSession();
        if (currentSession == null) { //Expired or no session
            System.out.println("No session");
            return "loginPage";
        } else if (currentSession.getAttribute("userObject") == null) {
            System.out.println("Invalid session");
            return "loginPage";
        }

        User user = (User)currentSession.getAttribute("userObject");

        //Get quiz types
        List<QuizType> quizTypeList = quizService.getAllQuizTypes();
        if (quizTypeList == null) {
            quizTypeList = new ArrayList<>();
        }

        model.addAttribute("user", user);
        model.addAttribute("quiz_types", quizTypeList);

        return "mainPage";
    }
}
