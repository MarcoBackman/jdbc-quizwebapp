package com.example.week3day13project.dao;

import com.example.week3day13project.domain.QuestionOption;

import java.util.List;

public interface QuestionOptionDAOScheme {
    List<QuestionOption> findAll();
    List<QuestionOption> findById(String optionID);
    void addOption(String optionContent, String isAnswer, String questionID);
    void deleteAllOption(String questionID);
    void editOption(String optionContent, String isAnswer, String questionID);
}
