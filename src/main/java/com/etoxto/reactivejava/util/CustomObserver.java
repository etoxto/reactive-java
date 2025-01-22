package com.etoxto.reactivejava.util;

import com.etoxto.reactivejava.model.ExamWork;
import com.etoxto.reactivejava.repository.DataRepository;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomObserver implements Observer<ExamWork> {

    private final DataRepository dataRepository;
    @Getter
    private Map<Long, HashSet<Long>> result;

    public CustomObserver(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        result =new ConcurrentHashMap<>();
    }

    @SneakyThrows
    @Override
    public void onNext(@NonNull ExamWork examWork) {
        dataRepository.loadDataFromDb();

        Long teacherId = examWork.getTeacher().getId();
        Long studentId = examWork.getStudent().getId();

        result.computeIfAbsent(teacherId, key -> new HashSet<>()).add(studentId);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Processing complete.");
    }
}
