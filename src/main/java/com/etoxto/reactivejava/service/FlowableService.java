package com.etoxto.reactivejava.service;

import com.etoxto.reactivejava.aop.Timed;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.model.ExamWork;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.util.BackpressureSubscriber;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Service
public class FlowableService {

    private final BackpressureSubscriber backpressureSubscriber;

    public FlowableService(BackpressureSubscriber backpressureSubscriber) {
        this.backpressureSubscriber = backpressureSubscriber;
    }

    @Timed(service = "Реактивный поток с кастомным Flowable")
    public Map<Long, HashSet<Long>> getResults(DataRepository dataRepository, ExamGrade examGrade) {
        ExecutorService pool = newFixedThreadPool(1);
        Scheduler scheduler = Schedulers.from(pool);

        Flowable<ExamWork> flowable = Flowable.fromIterable(dataRepository.getExamWorks())
                .filter(examWork -> examWork.getExamGrade().equals(examGrade));

        backpressureSubscriber.setExamGrade(examGrade);
        flowable.subscribeOn(scheduler).subscribe(backpressureSubscriber);

        return backpressureSubscriber.getResult();
    }
}
