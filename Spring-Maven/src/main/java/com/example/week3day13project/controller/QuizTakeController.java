package com.example.week3day13project.controller;

import com.example.week3day13project.domain.Question;
import com.example.week3day13project.domain.QuestionOption;
import com.example.week3day13project.domain.QuizType;
import com.example.week3day13project.domain.User;
import com.example.week3day13project.service.QuestionService;
import com.example.week3day13project.service.QuizLogService;
import com.example.week3day13project.service.QuizService;
import com.example.week3day13project.service.UserQuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class QuizTakeController {
    private final QuizService quizService;
    private final QuestionService questionService;
    private final QuizLogService quizLogService;
    private final UserQuestionService userQuestionService;
    static LinkedHashMap<Question, List<QuestionOption>> optionSetByQuestion;
    static int[] selectedAnswers;
    private String startDateTime = "";

    public QuizTakeController(QuizService quizService,
                              QuestionService questionService,
                              QuizLogService quizLogService,
                              UserQuestionService userQuestionService) {
        this.quizService = quizService;
        this.questionService = questionService;
        this.quizLogService = quizLogService;
        this.userQuestionService = userQuestionService;
    }

    @RequestMapping(value = "/take-quiz/{topic}", method = RequestMethod.GET)
    public String setupQuizTopicPage(@PathVariable(value="topic") String topic) {

        //record start time
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startDateTime = sdf.format(date);

        //Get question set
        optionSetByQuestion = questionService.getTenRandomQuetionsByType(topic);

        //initialize array
        selectedAnswers = new int[10];
        Arrays.fill(selectedAnswers, -1);

        return "redirect:" + topic + "/1";
    }

    @GetMapping(value = "/take-quiz/{topic}/{page}")
    public String setUpQuizPage(HttpServletRequest request,
                                Model model,
                                @PathVariable(value="topic") String topic,
                                @PathVariable(value="page") String questionNumber) {

        int questionIndex = new Integer(questionNumber) - 1;
        Question question = (Question) optionSetByQuestion.keySet().toArray()[questionIndex];
        model.addAttribute("topic", topic);
        model.addAttribute("question", question);
        model.addAttribute("options", optionSetByQuestion.get(question));
        model.addAttribute("questionNumber", questionIndex);
        model.addAttribute("selectedAnswer", selectedAnswers[questionIndex]);
        return "quizScreen";
    }

    @PostMapping(value = "/take-quiz/{topic}/{page}")
    public String saveProgress(HttpServletRequest request,
                               Model model,
                               @PathVariable(value="topic") String topic,
                               @PathVariable(value="page") String questionNumber,
                               @RequestParam(name="questionNumber") String actualQuestionNumber) {

        if (request.getParameterValues("is_answer") == null) {
            System.out.println("User hasn't selected an option");
            return "redirect:/take-quiz/" + topic + "/" + questionNumber;
        }
        Optional<String> value
                = Arrays.stream(request.getParameterValues("is_answer")).findAny();

        if (value.isPresent()) {
            int index = Integer.parseInt(value.get());
            selectedAnswers[Integer.parseInt(actualQuestionNumber)] = index;
        } else {
            System.out.println("User not selected");
        }

        model.addAttribute("selectedAnswer", selectedAnswers[Integer.parseInt(actualQuestionNumber)]);

        return "redirect:/take-quiz/" + topic + "/" + questionNumber;
    }

    @PostMapping(value = "/submit_quiz/{topic}")
    public String submitQuiz(HttpServletRequest request,
                             Model model,
                             @PathVariable(value="topic") String topic,
                             @RequestParam(name="questionNumber") String actualQuestionNumber) {

        HttpSession currentSession = request.getSession();
        User user = (User)currentSession.getAttribute("userObject");
        String[] param = request.getParameterValues("is_answer");
        if (param != null) {
            Optional<String> value
                    = Arrays.stream(request.getParameterValues("is_answer")).findAny();
            if (value.isPresent()) {
                int index = Integer.parseInt(value.get()) - 1;
                if (Integer.parseInt(actualQuestionNumber) == 0) {
                    selectedAnswers[Integer.parseInt(actualQuestionNumber)] = index;
                } else {
                    selectedAnswers[Integer.parseInt(actualQuestionNumber) - 1] = index;
                }
            }
        }


        //Measure end DateTime
        Date date = new Date(); //consider using java.time.Instant
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endDateTime = sdf.format(date);

        //Get quiz name
        int topicIndex = Integer.parseInt(topic);
        List<QuizType> quizTypes = quizService.getAllQuizTypes();
        Optional<QuizType> quizTypeOptional = quizTypes.stream().filter(quiz
                -> quiz.getQuizTypeNumber() == (topicIndex)).findFirst();

        QuizType quizType = null;

        if (quizTypeOptional.isPresent()) {
            quizType = quizTypeOptional.get();
            model.addAttribute("quizName", quizTypeOptional.get().getQuizDescription());
        } else {
            model.addAttribute("quizName", "");
        }
        model.addAttribute("user", user);
        model.addAttribute("startTime", startDateTime);
        model.addAttribute("endTime", endDateTime);

        //Crate Quiz Object first

        //Calculate result
        int totalScore = 0;
        //optionSetByQuestion - (LinkedHashMap<Question, List<QuestionOption>>)
        //selectedAnswers - int[10]
        List<Question> questionArray = new ArrayList<>(optionSetByQuestion.keySet());
        int[] actualAnswers = new int[10];
        for (int i = 0; i < questionArray.size(); i++) {
            List<QuestionOption> peekOptions = optionSetByQuestion.get(questionArray.get(i));

            //Create a data row into UserQuestion Table

            for (int j = 0; j < peekOptions.size(); j++) { //will iterate 10 times
                if (peekOptions.get(j).isAnswer()) {
                    actualAnswers[i] = j;
                    break;
                }
            }
        }
        //Compare with user answer
        for (int i = 0; i < actualAnswers.length; i++) {
            if (actualAnswers[i] == selectedAnswers[i]) {
                totalScore += 10;
            }
        }

        model.addAttribute("optionSetByQuestion", optionSetByQuestion);
        model.addAttribute("totalScore", totalScore);
        model.addAttribute("selectedAnswers", selectedAnswers);
        model.addAttribute("actualAnswers", actualAnswers);

        //create Quiz
        int quizID = quizService.createQuizObject(String.valueOf(quizType.getQuizTypeNumber()),
                String.valueOf(totalScore));

        //Create QuizLog
        quizLogService.createQuizLog(
                String.valueOf(user.getUserId()),
                startDateTime,
                endDateTime,
                String.valueOf(quizID));

        //Create UserQuestion for all questions
        // - only for multiple question: List<QuestionOption> peekOptions = optionS
        for (int i = 0; i < questionArray.size(); i++) {

            //Create a data row into UserQuestion Table
            userQuestionService.createUserQuestion(
                    String.valueOf(quizID), //QuizID
                    String.valueOf(questionArray.get(i).getQuestionID()),
                    "",//This is for a short question
                    String.valueOf(selectedAnswers[i]),
                    "0");
        }

        return "quizResult";
    }
}
