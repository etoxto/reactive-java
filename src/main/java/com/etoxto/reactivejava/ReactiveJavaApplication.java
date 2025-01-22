package com.etoxto.reactivejava;

import com.etoxto.reactivejava.config.DataConfig;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.service.CustomObservableService;
import com.etoxto.reactivejava.service.CustomParallelStreamService;
import com.etoxto.reactivejava.service.CustomSingleStreamService;
import com.etoxto.reactivejava.service.CustomSpliteratorStreamService;
import com.etoxto.reactivejava.service.FlowableService;
import com.etoxto.reactivejava.service.IterativeLoopService;
import com.etoxto.reactivejava.service.ParallelStreamService;
import com.etoxto.reactivejava.service.SingleStreamService;
import com.etoxto.reactivejava.util.RecordGenerator;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@AllArgsConstructor
@EnableAspectJAutoProxy
public class ReactiveJavaApplication {

    DataConfig dataConfig;
    DataRepository dataRepository;
    RecordGenerator recordGenerator;
    IterativeLoopService loopService;
    SingleStreamService singleStreamService;
    ParallelStreamService parallelStreamService;
    CustomParallelStreamService customParallelStreamService;
    CustomSingleStreamService customSingleStreamService;
    CustomSpliteratorStreamService customSpliteratorStreamService;
    CustomObservableService customObservableService;
    FlowableService flowableService;

    public static void main(String[] args) {
        SpringApplication.run(ReactiveJavaApplication.class, args);
    }

    @PostConstruct
    public void start() {
        for (var examWorkCount: dataConfig.getExamWorkCount()) {
            System.out.println("\nКоличество объектов: " + examWorkCount);
            dataRepository.setExamWorkMap(recordGenerator.generateExamWorks(examWorkCount));
            dataRepository.setDelayEnabled(dataConfig.isDelayEnabled());
            loopService.getResults(dataRepository, ExamGrade.D);
            singleStreamService.getResults(dataRepository, ExamGrade.D);
            parallelStreamService.getResults(dataRepository, ExamGrade.D);
            customSingleStreamService.getResults(dataRepository, ExamGrade.D);
            customParallelStreamService.getResults(dataRepository, ExamGrade.D);
            customSpliteratorStreamService.getResults(dataRepository, ExamGrade.D);
            customObservableService.getResults(dataRepository, ExamGrade.D);
            flowableService.getResults(dataRepository, ExamGrade.D);
        }
    }
}


// 2 лаба
// дописать однопоточный стрим со своим коллектором
// найти точку производительности между single and paralel stream
// написать свой fork join pull (compute method) сравнить с паралельным стримом
// вариант с сплитератор
