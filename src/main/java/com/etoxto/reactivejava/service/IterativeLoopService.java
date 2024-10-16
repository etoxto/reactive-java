package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class IterativeLoopService {

    public Map<Long, ArrayList<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        Map<Long, ArrayList<Long>> result = new HashMap<>();

        for (var examWork : dataRepository.getExamWorks()) {
            Long teacherId = examWork.getTeacher().getId();

            if (examWork.getExamGrade().equals(examGrade)) {
                Long studentId = examWork.getStudent().getId();
                if (result.containsKey(teacherId)) {
                    result.get(teacherId).add(studentId);
                } else {
                    ArrayList<Long> students = new ArrayList<>();
                    students.add(studentId);
                    result.put(teacherId, students);
                }
            }
        }

        return result;
    }
}
