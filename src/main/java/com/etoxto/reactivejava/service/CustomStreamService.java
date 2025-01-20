package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.aop.Timed;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.util.CustomCollector;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomStreamService {

    private final CustomCollector customCollector;

    @Timed(service = "Параллельный stream с кастомным коллектором")
    public Map<Long, HashSet<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        return dataRepository.getExamWorks().stream()
                .filter(e -> e.getExamGrade().equals(examGrade))
                .parallel()
                .collect(customCollector);
    }
}
