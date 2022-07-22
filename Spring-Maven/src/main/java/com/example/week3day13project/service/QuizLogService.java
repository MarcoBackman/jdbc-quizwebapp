package com.example.week3day13project.service;

import com.example.week3day13project.dao.implementation.*;
import com.example.week3day13project.domain.Quiz;
import com.example.week3day13project.domain.QuizLog;
import com.example.week3day13project.domain.QuizType;
import com.example.week3day13project.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class QuizLogService {

    private final QuizLogDAO quizLogDAO;
    private final QuizDAO quizDAO;
    private final QuizTypeDAO quizTypeDAO;
    private final UserDAO userDAO;

    @Autowired
    public QuizLogService(QuizLogDAO quizLogDAO, QuizDAO quizDAO, QuizTypeDAO quizTypeDAO, UserDAO userDAO) {
        this.quizLogDAO = quizLogDAO;
        this.quizDAO = quizDAO;
        this.quizTypeDAO = quizTypeDAO;
        this.userDAO = userDAO;
    }

    public void createQuizLog(String userID, String timeStart, String timeEnd, String quizID) {
        quizLogDAO.insertQuizLog(userID, timeStart, timeEnd, quizID);
    }

    public List<QuizLog> getAllQuizLog() {
        return quizLogDAO.findAll();
    }

    public QuizLog getQuizLogByQuiz(Quiz quiz) {
        return quizLogDAO.findByQuizID(String.valueOf(quiz.getQuizID()));
    }

    public List<QuizLog> getQuizLogByUserName(String userName) {
        return quizLogDAO.findByUserID(userName);
    }

    public List<QuizLog> getQuizLogByUserID(String userID) {
        return quizLogDAO.findByUserID(userID);
    }


    public QuizType getTypeDescriptionByQuiz(Quiz quiz) {
        return quizTypeDAO.findTypeByTypeNumber(String.valueOf(quiz.getQuizType()));
    }

    public User getUserByQuizLog(QuizLog quizLog) {
        Optional<User> candidate = userDAO.findById(String.valueOf(quizLog.getUserID())).stream().findAny();
        return candidate.get();
    }

    public List<QuizType> getAllQuizTypes() {
        return quizTypeDAO.getAllQuizTypes();
    }

}
