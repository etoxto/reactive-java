package com.etoxto.reactivejava.repository;

import com.etoxto.reactivejava.model.ExamWork;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Data
@Service
public class DataRepository {
    private Map<Long, ExamWork> examWorkMap;
    private boolean delayEnabled;

    public Collection<ExamWork> getExamWorks() {
        return examWorkMap.values();
    }

    public void loadDataFromDb() throws InterruptedException {
        if (delayEnabled) {
            Thread.sleep(0, 1);
        }
    }
}
