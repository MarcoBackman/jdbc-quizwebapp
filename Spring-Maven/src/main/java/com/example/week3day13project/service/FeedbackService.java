package com.example.week3day13project.service;

import com.example.week3day13project.dao.implementation.FeedbackDAO;
import com.example.week3day13project.dao.implementation.UserDAO;
import com.example.week3day13project.domain.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackDAO feedbackDAO;
    private final UserDAO userDAO;

    @Autowired
    public FeedbackService(FeedbackDAO feedbackDAO, UserDAO userDAO) {
        this.feedbackDAO = feedbackDAO;
        this.userDAO = userDAO;
    }

    public boolean allowedToFeedback(String user_id) {
        return feedbackDAO.checkFeedbackAvailability(user_id);
    }

    public boolean leaveFeedBack(String rate, String content) {
        return feedbackDAO.uploadFeedback(rate, content);
    }

    public boolean updateUserFeedbackStatus(String user_id) {
        return userDAO.setHasRated(user_id);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackDAO.findAll();
    }
}
