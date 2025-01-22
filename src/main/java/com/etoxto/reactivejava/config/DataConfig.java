package com.etoxto.reactivejava.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "data")
public class DataConfig {
    private List<Integer> examWorkCount;
    private boolean delayEnabled;
    private int teacherCount;
    private int studentCount;
}
