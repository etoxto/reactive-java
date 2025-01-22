package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.aop.Timed;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class IterativeLoopService {

    @Timed(service = "Преобразование через цикл")
    public Map<Long, HashSet<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        Map<Long, HashSet<Long>> result = new HashMap<>();

        for (var examWork : dataRepository.getExamWorks()) {
            Long teacherId = examWork.getTeacher().getId();

            if (examWork.getExamGrade().equals(examGrade)) {
                Long studentId = examWork.getStudent().getId();
                if (result.containsKey(teacherId)) {
                    result.get(teacherId).add(studentId);
                } else {
                    HashSet<Long> students = new HashSet<>();
                    students.add(studentId);
                    result.put(teacherId, students);
                }
            }
        }

        return result;
    }
}
