package com.example.week3day13project.domain;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QuizType {
    private int quizTypeNumber;
    private String quizDescription;
}
