package com.etoxto.reactivejava.util;

import com.etoxto.reactivejava.model.ExamWork;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@Component
public class CustomParallelCollector implements Collector<ExamWork, Map<Long, Set<Long>>, Map<Long, Set<Long>>> {

    @Override
    public Supplier<Map<Long, Set<Long>>> supplier() {
        return ConcurrentHashMap::new;
    }

    @Override
    public BiConsumer<Map<Long, Set<Long>>, ExamWork> accumulator() {
        return (res, examWork) -> {
            Long teacherId = examWork.getTeacher().getId();
            Long studentId = examWork.getId();
            res.computeIfAbsent(teacherId, k -> new ConcurrentSkipListSet<>()).add(studentId);
        };
    }

    @Override
    public BinaryOperator<Map<Long, Set<Long>>> combiner() {
        return (map1, map2) -> {
            map2.forEach((teacherId, students) ->
                    map1.merge(teacherId, students, (existing, replacement) -> {
                        existing.addAll(replacement);
                        return existing;
                    })
            );
            return map1;
        };
    }

    @Override
    public Function<Map<Long, Set<Long>>, Map<Long, Set<Long>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(
                Characteristics.CONCURRENT,
                Characteristics.IDENTITY_FINISH
        );
    }
}
