package com.lfl.advent2020;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class AdventApplication implements CommandLineRunner {
    private static final String input = "day4-1.txt";
    private static final String service = "passportValidator";

    private final Map<String, LinesConsumer> serviceByName;

    public AdventApplication(ApplicationContext context) {
        serviceByName = context.getBeansOfType(LinesConsumer.class);

        if (!serviceByName.containsKey(service)) {
            log.warn("Service {} is not recognised. The set of recognised service is {}", service, serviceByName.keySet());
        }
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AdventApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        Instant start = Instant.now();
        log.info("Start run");

        runService();

        log.info("End run");
        Instant end = Instant.now();
        log.info("Elapsed time = {}ms", Duration.between(start, end).toMillis());
    }

    private void runService() throws Exception {
        serviceByName.getOrDefault(service, LinesConsumer.doNothing())
                     .consume(LinesConsumer.readAllInput(input));
    }
}
