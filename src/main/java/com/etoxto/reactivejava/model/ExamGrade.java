package com.etoxto.reactivejava.model;

import lombok.Getter;

@Getter
public enum ExamGrade {
    A(new GradeTable(95, 100)),
    B(new GradeTable(80, 94)),
    C(new GradeTable(60, 79)),
    D(new GradeTable(0, 59));

    final GradeTable gradeTable;

    ExamGrade(GradeTable gradeTable) {
        this.gradeTable = gradeTable;
    }

    public record GradeTable(int start, int end) {
    }

    public static ExamGrade valueOf(int code) {
        return switch (code) {
            case 0 -> A;
            case 1 -> B;
            case 2 -> C;
            case 3 -> D;
            default -> throw new IllegalArgumentException("Grade not supported");
        };
    }
}
