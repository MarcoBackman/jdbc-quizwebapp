package com.example.week3day13project.service;

import com.example.week3day13project.dao.implementation.QuestionDAO;
import com.example.week3day13project.dao.implementation.QuestionOptionDAO;
import com.example.week3day13project.domain.Question;
import com.example.week3day13project.domain.QuestionOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {

    private final QuestionDAO questionDAO;
    private final QuestionOptionDAO questionOptionDAO;

    @Autowired
    public QuestionService(QuestionDAO questionDAO, QuestionOptionDAO questionOptionDAO) {
        this.questionDAO = questionDAO;
        this.questionOptionDAO = questionOptionDAO;
    }

    public List<Question> getAllMultipleQuestionsByType(String questionType) {
        return questionDAO.findAllMultipleQuestionByType(questionType);
    }

    public List<Question> getAllShortQuestionsByType(String questionType) {
        return questionDAO.findAllShortQuestionByType(questionType);
    }

    public void addMultipleQuestionWithOptions(String questionContent,
                                               String questionType,
                                               List<QuestionOption> options) {
        //Create option-empty question first
        int questionIndex = questionDAO.addMultipleQuestion(questionContent, questionType);

        //Map options to question index on creation
        options.forEach(option -> questionOptionDAO.addOption(option.getOptionContent(),
                (option.isAnswer() ? "1" : "0"), String.valueOf(questionIndex)));

    }

    public int addShortQuestion(String questionContent,
                                String answer,
                                String questionType) {
        return questionDAO.addShortQuestion(questionContent, answer, questionType);
    }

    //Currently, Cannot change options.
    public void changeMultipleQuestion(String questionID, String questionContent) {
        questionDAO.updateMultipleQuestionContent(questionID, questionContent);
    }

    public void deleteMultipleQuestion(String questionID) {
        //delete question
        questionDAO.deleteQuestion(questionID);
        questionOptionDAO.deleteAllOption(questionID);
    }

    public void disableQuestion(String questionID) {
        //delete question
        questionDAO.disableQuestion(questionID);
    }

    public void activateQuestion(String questionID) {
        //delete question
        questionDAO.activateQuestion(questionID);
    }

    public Map<Integer, List<QuestionOption>> getAllOptionsByGroup() {
        //delete question
        return questionOptionDAO.findAllAndGroupByQuestionID();
    }

    public LinkedHashMap<Question, List<QuestionOption>> getTenRandomQuetionsByType(String questionType) {
        return questionDAO.getTenMultipleQuestionsByQuizType(questionType);
    }

    public Question getQuestionByQuestionID(String questionID) {
        return questionDAO.findById(questionID).stream().findFirst().orElse(null);
    }

    public List<QuestionOption> getQuestionOptionsByQuestionID(String questionID) {
        return questionOptionDAO.getOptionsByQuestionID(questionID);
    }

}
