package com.etoxto.reactivejava;

import com.etoxto.reactivejava.config.DataConfig;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.service.CustomStreamService;
import com.etoxto.reactivejava.service.IterativeLoopService;
import com.etoxto.reactivejava.service.ParallelStreamService;
import com.etoxto.reactivejava.service.SingleStreamService;
import com.etoxto.reactivejava.util.RecordGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveJavaApplication {

    DataConfig dataConfig;
    DataRepository dataRepository;
    RecordGenerator recordGenerator;
    IterativeLoopService loopService;
    SingleStreamService singleStreamService;
    ParallelStreamService parallelStreamService;
    CustomStreamService customStreamService;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveJavaApplication.class, args);
    }

    @PostConstruct
    public void start() {
        for (var examWorkCount: dataConfig.getExamWorkCount()) {
            System.out.println("\nКоличество объектов: " + examWorkCount);
            dataRepository.setExamWorkMap(recordGenerator.generateExamWorks(examWorkCount));
            loopService.getResults(dataRepository, ExamGrade.D);
            singleStreamService.getResults(dataRepository, ExamGrade.D);
            parallelStreamService.getResults(dataRepository, ExamGrade.D);
            customStreamService.getResults(dataRepository, ExamGrade.D);
        }
    }
}
