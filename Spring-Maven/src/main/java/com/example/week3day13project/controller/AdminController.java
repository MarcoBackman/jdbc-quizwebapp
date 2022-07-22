package com.example.week3day13project.controller;

import com.example.week3day13project.domain.*;
import com.example.week3day13project.service.*;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @TODO: Credential check path is needed instead of checking credential on every mapper
 */
@Controller
public class AdminController {

    private final UserInfoService userInfoService;
    private final FeedbackService feedbackService;
    private final QuizLogService quizLogService;
    private final QuizService quizService;
    private final QuestionService questionService;
    private final UserQuestionService userQuestionService;

    public AdminController(UserInfoService userInfoService,
                           FeedbackService feedbackService,
                           QuizLogService quizLogService,
                           QuizService quizService,
                           QuestionService questionService,
                           UserQuestionService userQuestionService) {
        this.userInfoService = userInfoService;
        this.feedbackService = feedbackService;
        this.quizLogService = quizLogService;
        this.quizService = quizService;
        this.questionService = questionService;
        this.userQuestionService = userQuestionService;
    }


    private boolean isActiveSession(HttpServletRequest request) {
        HttpSession currentSession = request.getSession(false);
        if (currentSession == null) { //Expired or no session
            return false;
        } else if (currentSession.getAttribute("userObject") == null) {
            return false;
        }
        return true;
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession currentSession = request.getSession(false);
        //identify user credential
        User user = (User) currentSession.getAttribute("userObject");
        if (!user.isAdmin() || user.isSuspended()) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/admin-page")
    public String getAdminPage(HttpServletRequest request, Model model) {
        if (!isActiveSession(request)) {
            return "loginPage";
        } else if (!isAdmin(request)) {
            return "redirect:mainPage";
        }

        //list all users
        List<User> userLists = userInfoService.getAllUsers();
        model.addAttribute("users", userLists);

        //view all feedback
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        model.addAttribute("feedbacks", feedbackList);

        //for filter
        List<QuizType> quizTypes = quizLogService.getAllQuizTypes();
        model.addAttribute("quizTypes", quizTypes);

        //list all quiz logs
        List<QuizLog> quizLogs = quizLogService.getAllQuizLog(); //this is already ordered by date
        LinkedHashMap<QuizLog, Quiz> quizLinkedMapByQuizLog = new LinkedHashMap<>();
        LinkedHashMap<Quiz, QuizType> typeLinkedMapByQuiz = new LinkedHashMap<>();
        LinkedHashMap<Quiz, User> userLinkedMapByQuiz = new LinkedHashMap<>();

        for (QuizLog quizLog : quizLogs) {
            //returns Quiz
            Quiz result = quizService.getQuizByID(String.valueOf(quizLog.getQuizID()));
            quizLinkedMapByQuizLog.put(quizLog, result);

            //find TypeDescription by Quiz
            QuizType description = quizLogService.getTypeDescriptionByQuiz(result);
            //And put it into the Map
            typeLinkedMapByQuiz.put(result, description);

            //find user by QuizLog
            User targetUser = quizLogService.getUserByQuizLog(quizLog);
            //put that user by quiz
            userLinkedMapByQuiz.put(result, targetUser);
        }

        model.addAttribute("quizLinkedMapByQuizLog", quizLinkedMapByQuizLog);
        model.addAttribute("typeLinkedMapByQuiz", typeLinkedMapByQuiz);
        model.addAttribute("userLinkedMapByQuiz", userLinkedMapByQuiz);

        return "adminPage";
    }

    //view log detail in logDetailPage
    @GetMapping(value = "/admin-page/log-detail/{quiz_id}")
    public String viewDetail(HttpServletRequest request,
                             Model model,
                             @PathVariable(value="quiz_id") String quizID) {
        if (!isActiveSession(request)) {
            return "loginPage";
        } else if (!isAdmin(request)) {
            return "redirect:mainPage";
        }

        //Initialize storage
        LinkedHashMap<Question, List<QuestionOption>> optionSetByQuestion = new LinkedHashMap<>();
        int[] selectedAnswers = new int[10];
        //Get UserQuestions by quiz
        List<UserQuestion> userQuestions = userQuestionService.getUserQuestionByQuizID(quizID);
        System.out.println(userQuestions.size());
        int index = 0;
        for (UserQuestion userQuestion : userQuestions) {
            selectedAnswers[index] = userQuestion.getSelected_option_id();

            Question tempQuestion = questionService.getQuestionByQuestionID(String.valueOf(userQuestion.getQuestionID()));
            List<QuestionOption> tempOptions
                    = questionService.getQuestionOptionsByQuestionID(String.valueOf(tempQuestion.getQuestionID()));
            optionSetByQuestion.put(tempQuestion, tempOptions);
            index++;
        }

        model.addAttribute("optionSetByQuestion", optionSetByQuestion);
        model.addAttribute("selectedAnswers", selectedAnswers);

        return "/logDetailPage";
    }

    //filter
    @PostMapping(value = "/admin-page/by-category")
    public String getOnlyCategory(HttpServletRequest request,
                                  Model model) {
        if (!isActiveSession(request)) {
            return "loginPage";
        } else if (!isAdmin(request)) {
            return "redirect:mainPage";
        }


        if (request.getParameterValues("filterByQuizType") == null) {
            return "redirect:/adminPage;";
        }

        Optional<String> value
                = Arrays.stream(request.getParameterValues("filterByQuizType")).findAny();
        int typeIndex = -1;
        if (value.isPresent()) {
            typeIndex = Integer.parseInt(value.get());
        } else {
            return "redirect:/adminPage;";
        }

        //list all users
        List<User> userLists = userInfoService.getAllUsers();
        model.addAttribute("users", userLists);

        //view all feedback
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        model.addAttribute("feedbacks", feedbackList);

        //for filter
        List<QuizType> quizTypes = quizLogService.getAllQuizTypes();
        model.addAttribute("quizTypes", quizTypes);

        //list quizzes by category
        List<Quiz> quizzes = quizService.getQuizByType(String.valueOf(typeIndex)); //this is already ordered by date

        List<QuizLog> quizLogs = new ArrayList<>();

        //List QuizLog by quizzes
        for (Quiz quiz : quizzes) {
            quizLogs.add(quizLogService.getQuizLogByQuiz(quiz));
        }


        //Date sort
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        quizLogs.sort((o1, o2) -> {
            try {
                Date o1Date = sdf.parse(o1.getTimeStart());
                Date o2Date = sdf.parse(o2.getTimeStart());
                return Integer.compare(0, o1Date.compareTo(o2Date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });

        LinkedHashMap<QuizLog, Quiz> quizLinkedMapByQuizLog = new LinkedHashMap<>();
        LinkedHashMap<Quiz, QuizType> typeLinkedMapByQuiz = new LinkedHashMap<>();
        LinkedHashMap<Quiz, User> userLinkedMapByQuiz = new LinkedHashMap<>();

        for (QuizLog quizLog : quizLogs) {
            //returns Quiz
            Quiz result = quizService.getQuizByID(String.valueOf(quizLog.getQuizID()));
            quizLinkedMapByQuizLog.put(quizLog, result);

            //find TypeDescription by Quiz
            QuizType description = quizLogService.getTypeDescriptionByQuiz(result);
            //And put it into the Map
            typeLinkedMapByQuiz.put(result, description);

            //find user by QuizLog
            User targetUser = quizLogService.getUserByQuizLog(quizLog);
            //put that user by quiz
            userLinkedMapByQuiz.put(result, targetUser);
        }

        model.addAttribute("quizLinkedMapByQuizLog", quizLinkedMapByQuizLog);
        model.addAttribute("typeLinkedMapByQuiz", typeLinkedMapByQuiz);
        model.addAttribute("userLinkedMapByQuiz", userLinkedMapByQuiz);
        return "adminPage";
    }

    //filter
    @PostMapping(value = "/admin-page/by-user")
    public String getUserQuizLog(HttpServletRequest request,
                                 Model model) {
        if (!isActiveSession(request)) {
            return "loginPage";
        } else if (!isAdmin(request)) {
            return "redirect:mainPage";
        }

        if (request.getParameterValues("user_id") == null) {
            return "redirect:/adminPage;";
        }

        Optional<String> value
                = Arrays.stream(request.getParameterValues("user_id")).findAny();
        String userID = "";
        if (value.isPresent()) {
            userID = value.get();
        } else {
            return "redirect:/adminPage;";
        }

        //list all users
        List<User> userLists = userInfoService.getAllUsers();
        model.addAttribute("users", userLists);

        //view all feedback
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        model.addAttribute("feedbacks", feedbackList);

        //for filter
        List<QuizType> quizTypes = quizLogService.getAllQuizTypes();
        model.addAttribute("quizTypes", quizTypes);

        //list quizzes by user
        List<QuizLog> quizLogs = quizLogService.getQuizLogByUserID(userID); //this is already ordered by date(desc)

        LinkedHashMap<QuizLog, Quiz> quizLinkedMapByQuizLog = new LinkedHashMap<>();
        LinkedHashMap<Quiz, QuizType> typeLinkedMapByQuiz = new LinkedHashMap<>();
        LinkedHashMap<Quiz, User> userLinkedMapByQuiz = new LinkedHashMap<>();

        for (QuizLog quizLog : quizLogs) {
            //returns Quiz
            Quiz result = quizService.getQuizByID(String.valueOf(quizLog.getQuizID()));
            quizLinkedMapByQuizLog.put(quizLog, result);

            //find TypeDescription by Quiz
            QuizType description = quizLogService.getTypeDescriptionByQuiz(result);
            //And put it into the Map
            typeLinkedMapByQuiz.put(result, description);

            //find user by QuizLog
            User targetUser = quizLogService.getUserByQuizLog(quizLog);
            //put that user by quiz
            userLinkedMapByQuiz.put(result, targetUser);
        }

        model.addAttribute("quizLinkedMapByQuizLog", quizLinkedMapByQuizLog);
        model.addAttribute("typeLinkedMapByQuiz", typeLinkedMapByQuiz);
        model.addAttribute("userLinkedMapByQuiz", userLinkedMapByQuiz);

        return "adminPage";
    }
}
