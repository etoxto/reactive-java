package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.aop.Timed;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.util.CustomParallelCollector;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class CustomParallelStreamService {

    private final CustomParallelCollector customParallelCollector;

    @Timed(service = "Параллельный stream с кастомным коллектором")
    public Map<Long, Set<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        return dataRepository.getExamWorks().stream()
                .peek(examWork -> {
                    try {
                        dataRepository.loadDataFromDb();
                    }  catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .parallel()
                .filter(e -> e.getExamGrade().equals(examGrade))
                .collect(customParallelCollector);
    }
}
