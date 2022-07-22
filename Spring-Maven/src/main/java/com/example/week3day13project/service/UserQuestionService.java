package com.example.week3day13project.service;

import com.example.week3day13project.dao.implementation.QuizDAO;
import com.example.week3day13project.dao.implementation.UserQuestionDAO;
import com.example.week3day13project.domain.UserQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQuestionService {
    private final QuizDAO quizDao;
    private final UserQuestionDAO userQuestionDAO;

    @Autowired
    public UserQuestionService(QuizDAO quizDao,
                                UserQuestionDAO userQuestionDAO) {
        this.quizDao = quizDao;
        this.userQuestionDAO = userQuestionDAO;
    }

    public void createUserQuestion(String quizID,
                                   String questionID,
                                   String userAnswer,
                                   String selectedOptionID,
                                   String isShortQuestion) {
        userQuestionDAO.insertUserQuestion(quizID, questionID, userAnswer, selectedOptionID, isShortQuestion);
    }

    public List<UserQuestion> getUserQuestionByQuizID(String quizID) {
        return userQuestionDAO.findAllByQuizID(quizID);
    }

}
