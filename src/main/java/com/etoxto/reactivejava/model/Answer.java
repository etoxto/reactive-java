package com.etoxto.reactivejava.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Answer {
    private Long id;
    private String result;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double score;
}
