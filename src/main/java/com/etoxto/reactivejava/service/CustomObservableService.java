package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.aop.Timed;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.model.ExamWork;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.util.CustomObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class CustomObservableService {

    private final CustomObserver customObserver;

    public CustomObservableService(CustomObserver customObserver) {
        this.customObserver = customObserver;
    }

    @Timed(service = "Реактивный поток с кастомным Observer")
    public Map<Long, HashSet<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Scheduler scheduler = Schedulers.from(pool);

        Observable<ExamWork> observable = Observable.fromIterable(dataRepository.getExamWorks())
                .filter(examWork -> examWork.getExamGrade().equals(examGrade));

        observable
                .subscribeOn(scheduler)
                .subscribe(customObserver);

        /*pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }*/

        return customObserver.getResult();
    }
}
