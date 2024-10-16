package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.util.CustomCollector;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomStreamService {

    private final CustomCollector customCollector;

    public Map<Long, ArrayList<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        return dataRepository.getExamWorks().stream()
                .filter(e -> e.getExamGrade().equals(examGrade))
                .parallel()
                .collect(customCollector);
    }
}
