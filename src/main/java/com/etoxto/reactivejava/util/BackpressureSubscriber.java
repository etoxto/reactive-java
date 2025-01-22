package com.etoxto.reactivejava.util;

import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.model.ExamWork;
import com.etoxto.reactivejava.repository.DataRepository;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BackpressureSubscriber implements FlowableSubscriber<ExamWork> {

    private final long BATCH_SIZE = 10;
    private Subscription subscription;
    @Setter
    private ExamGrade examGrade;

    private final DataRepository dataRepository;

    @Getter
    private final Map<Long, HashSet<Long>> result = new ConcurrentHashMap<>();

    public BackpressureSubscriber(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        subscription.request(BATCH_SIZE);
    }

    @SneakyThrows
    @Override
    public void onNext(ExamWork examWork) {
        dataRepository.loadDataFromDb();

        Long teacherId = examWork.getTeacher().getId();

        if (examWork.getExamGrade().equals(examGrade)) {
            Long studentId = examWork.getStudent().getId();
            result.computeIfAbsent(teacherId, k -> new HashSet<>()).add(studentId);
        }

        subscription.request(BATCH_SIZE);
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println("Произошла ошибка: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Обработка завершена!");
    }
}
