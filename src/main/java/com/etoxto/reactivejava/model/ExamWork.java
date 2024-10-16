package com.etoxto.reactivejava.model;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;

@Data
@Builder
public class ExamWork {
    private Long id;
    private Person student;
    private Person teacher;
    private int attempts;
    private LinkedList<Answer> answers;
    private ExamGrade examGrade;
}
