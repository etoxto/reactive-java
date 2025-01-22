package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.aop.Timed;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.util.CustomSingleCollector;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomSingleStreamService {

    private final CustomSingleCollector customSingleCollector;

    @Timed(service = "Однопоточный stream с кастомным коллектором")
    public Map<Long, HashSet<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        return dataRepository.getExamWorks().stream()
                .peek(examWork -> {
                    try {
                        dataRepository.loadDataFromDb();
                    }  catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(e -> e.getExamGrade().equals(examGrade))
                .collect(customSingleCollector);
    }
}
