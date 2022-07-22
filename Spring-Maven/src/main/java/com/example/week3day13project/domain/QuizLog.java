package com.example.week3day13project.domain;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuizLog {
    private int logID;
    private int userID;
    private String timeStart;
    private String timeEnd;
    private int quizID;
}
