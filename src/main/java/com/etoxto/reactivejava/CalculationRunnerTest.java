package com.etoxto.reactivejava;

import com.etoxto.reactivejava.config.DataConfig;
import com.etoxto.reactivejava.model.ExamGrade;
import com.etoxto.reactivejava.repository.DataRepository;
import com.etoxto.reactivejava.service.CustomParallelStreamService;
import com.etoxto.reactivejava.service.CustomSpliteratorStreamService;
import com.etoxto.reactivejava.service.ParallelStreamService;
import com.etoxto.reactivejava.util.CustomParallelCollector;
import com.etoxto.reactivejava.util.RecordGenerator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class CalculationRunnerTest {

    private DataConfig dataConfig;
    private RecordGenerator recordGenerator;
    private DataRepository dataRepository;
    private ExamGrade examGrade;
    private CustomParallelCollector collector;
    private CustomParallelCollector collector2;
    private CustomParallelStreamService parallelStreamService;
    private CustomSpliteratorStreamService spliteratorStreamService;
    private ParallelStreamService streamService;

    @Setup(Level.Iteration)
    public void setUp() {
        dataConfig = new DataConfig();
        dataConfig.setTeacherCount(100);
        dataConfig.setStudentCount(1000);
        recordGenerator = new RecordGenerator(dataConfig);
        dataRepository = new DataRepository();
        dataRepository.setExamWorkMap(recordGenerator.generateExamWorks(50000));
        examGrade = ExamGrade.D;
        collector = new CustomParallelCollector();
        collector2 = new CustomParallelCollector();
        spliteratorStreamService = new CustomSpliteratorStreamService(collector);
        parallelStreamService = new CustomParallelStreamService(collector2);
        streamService = new ParallelStreamService();
    }

    @Benchmark
    public void testCustomSpliteratorStreamService() {
        spliteratorStreamService.getResults(dataRepository, examGrade);
    }

    @Benchmark
    public void testCustomParallelStreamService() {
        parallelStreamService.getResults(dataRepository, examGrade);
    }

    @Benchmark
    public void testParallelStreamService() {
        streamService.getResults(dataRepository, examGrade);
    }
}
