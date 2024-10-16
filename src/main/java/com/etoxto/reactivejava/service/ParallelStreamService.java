package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParallelStreamService {

    public Map<Long, ArrayList<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        return dataRepository.getExamWorks().stream()
                .filter(e -> e.getExamGrade().equals(examGrade))
                .parallel()
                .collect(Collectors.toConcurrentMap(
                        e -> e.getTeacher().getId(),
                        e -> {
                            ArrayList<Long> students = new ArrayList<>();
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
