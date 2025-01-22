package com.etoxto.reactivejava.util;

import com.etoxto.reactivejava.model.ExamWork;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@Component
public class CustomSingleCollector implements Collector<ExamWork, Map<Long, HashSet<Long>>, Map<Long, HashSet<Long>>> {

    @Override
    public Supplier<Map<Long, HashSet<Long>>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<Long, HashSet<Long>>, ExamWork> accumulator() {
        return (res, examWork) -> {
            Long teacherId = examWork.getTeacher().getId();
            Long studentId = examWork.getId();
            res.computeIfAbsent(teacherId, k -> new HashSet<>()).add(studentId);
        };
    }

    @Override
    public BinaryOperator<Map<Long, HashSet<Long>>> combiner() {
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
    public Function<Map<Long, HashSet<Long>>, Map<Long, HashSet<Long>>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.IDENTITY_FINISH);
    }
}
