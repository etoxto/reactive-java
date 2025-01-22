package com.etoxto.reactivejava.util;

import com.etoxto.reactivejava.model.ExamWork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class CustomSpliterator implements Spliterator<ExamWork> {

    private final List<ExamWork> examWorks;
    private int currentPosition = 0;

    public CustomSpliterator(Collection<ExamWork> examWorks) {
        this.examWorks = new ArrayList<>(examWorks);
        this.currentPosition = 0;
    }

    @Override
    public boolean tryAdvance(Consumer<? super ExamWork> action) {
        if (currentPosition < examWorks.size()) {
            ExamWork current = examWorks.get(currentPosition++);
            if (current != null) {
                action.accept(current);
            }
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<ExamWork> trySplit() {
        int size = (int) estimateSize();
        if (size < 2) {
            return null;
        }

        int splitPosition = currentPosition + size / 2;
        currentPosition = splitPosition;
        List<ExamWork> subList = examWorks.subList(currentPosition, splitPosition);
        return new CustomSpliterator(subList);
    }

    @Override
    public long estimateSize() {
        return examWorks.size() - currentPosition;
    }

    @Override
    public int characteristics() {
        return SIZED | SUBSIZED | NONNULL | IMMUTABLE;
    }
}
