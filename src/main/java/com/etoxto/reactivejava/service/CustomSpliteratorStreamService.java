package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.aop.Timed;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.util.CustomParallelCollector;
import com.etoxto.reactivejava.util.CustomSpliterator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CustomSpliteratorStreamService {

    private final CustomParallelCollector customParallelCollector;

    @Timed(service = "Параллельный stream с кастомным сплитератором")
    public Map<Long, Set<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        return StreamSupport.stream(new CustomSpliterator(dataRepository.getExamWorks()), true)
                .peek(examWork -> {
                    try {
                        dataRepository.loadDataFromDb();
                    }  catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(examWork -> examWork.getExamGrade().equals(examGrade))
                .collect(customParallelCollector);
    }
}
