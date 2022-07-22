package com.example.week3day13project.service;

import com.example.week3day13project.dao.implementation.QuizDAO;
import com.example.week3day13project.dao.implementation.QuizTypeDAO;
import com.example.week3day13project.dao.implementation.UserDAO;
import com.example.week3day13project.domain.Question;
import com.example.week3day13project.domain.Quiz;
import com.example.week3day13project.domain.QuizType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizTypeDAO quizTypeDAO;
    private final QuizDAO quizDAO;

    @Autowired
    public QuizService(QuizTypeDAO quizTypeDAO, QuizDAO quizDAO) {
        this.quizTypeDAO = quizTypeDAO;
        this.quizDAO = quizDAO;
    }

    public List<QuizType> getAllQuizTypes() {
        return quizTypeDAO.getAllQuizTypes();
    }

    //Add score to quiz log
    /**
     * This creates a quiz object and store it into DB
     * @return Integer quiz_id
     */
    public int createQuizObject(String quizTypeIndex, String score) {
        return quizDAO.createQuiz(quizTypeIndex, score);
    }

    //This will only return one Quiz set since quizID is unique
    public Quiz getQuizByID(String quizID) {
        Optional<Quiz> quizCandidate = quizDAO.findByID(quizID).stream().findFirst();
        //Just throw empty quiz if not present
        return quizCandidate.orElseGet(Quiz::new);
    }

    public List<Quiz> getQuizByType(String quizTypeIndex) {
        return quizDAO.findByQuizTypeIndex(quizTypeIndex);
    }
}
