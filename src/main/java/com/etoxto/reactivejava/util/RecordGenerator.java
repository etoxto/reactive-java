package com.etoxto.reactivejava.util;

import com.etoxto.reactivejava.config.DataConfig;
import com.etoxto.reactivejava.model.Answer;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.model.ExamWork;
import com.etoxto.reactivejava.model.Person;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
@RequiredArgsConstructor
public class RecordGenerator {

    private final DataConfig dataConfig;

    private final Faker faker = new Faker();

    private final Random random = new Random();

    private List<Person> generateStudents() {
        return LongStream
                .rangeClosed(dataConfig.getTeacherCount() + 1, dataConfig.getStudentCount() + dataConfig.getTeacherCount())
                .mapToObj(x -> Person.builder()
                        .id(x)
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .build())
                .collect(Collectors.toList());

    }

    private List<Person> generateTeachers() {
        return LongStream
                .rangeClosed(0, dataConfig.getTeacherCount())
                .mapToObj(x -> Person.builder()
                        .id(x)
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<Answer> generateAnswers(int examWorkCount) {
        return LongStream
                .rangeClosed(0, examWorkCount / 10)
                .mapToObj(x -> {
                    var time = LocalDateTime.now();

                    return Answer.builder()
                            .id(x)
                            .result("Result" + x)
                            .startTime(time)
                            .endTime(time.plusMinutes(10 + random.nextInt(41)))
                            .score(random.nextInt(101))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Map<Long, ExamWork> generateExamWorks(int examWorkCount) {
        var students = generateStudents();
        var teachers = generateTeachers();
        var answers = generateAnswers(examWorkCount);

        return LongStream
                .rangeClosed(0, examWorkCount)
                .mapToObj(x -> {
                    int attempts = new Random().nextInt(4) + 1;
                    Set<Integer> selectedIndices = new HashSet<>();
                    LinkedList<Answer> selectedAnswers = new LinkedList<>();

                    for (int i = 0; i < attempts; i++) {
                        int index = random.nextInt(answers.size());

                        if (!selectedIndices.contains(index)) {
                            selectedAnswers.add(answers.get(index));
                            selectedIndices.add(index);
                        }
                    }

                    Answer lastAnswer = selectedAnswers.getLast();
                    ExamGrade examGrade = Arrays.stream(ExamGrade.values())
                            .filter(grade -> lastAnswer.getScore() >= grade.getGradeTable().start() &&
                                    lastAnswer.getScore() <= grade.getGradeTable().end())
                            .findFirst()
                            .orElse(ExamGrade.D);

                    return ExamWork.builder()
                            .id(x)
                            .student(students.get(teachers.size() + random.nextInt(students.size())))
                            .teacher(teachers.get(random.nextInt(teachers.size())))
                            .attempts(attempts)
                            .answers(selectedAnswers)
                            .examGrade(examGrade)
                            .build();
                })
                .collect(Collectors.toMap(
                        ExamWork::getId,
                        Function.identity()
                ));
    }
}
