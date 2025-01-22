package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.aop.Timed;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParallelStreamService {

    @Timed(service = "Параллельный stream с стандартным коллектором ")
    public Map<Long, HashSet<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        return dataRepository.getExamWorks().stream()
                .parallel()
                .peek(examWork -> {
                    try {
                        dataRepository.loadDataFromDb();
                    }  catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(e -> e.getExamGrade().equals(examGrade))
                .collect(Collectors.toConcurrentMap(
                        e -> e.getTeacher().getId(),
                        e -> {
                            HashSet<Long> students = new HashSet<>();
                            students.add(e.getId());
                            return students;
                        },
                        (existing, replacement) -> {
                            existing.addAll(replacement);
                            return existing;
                        }
                ));
    }
}
