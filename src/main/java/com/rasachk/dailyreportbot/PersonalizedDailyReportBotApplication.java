package com.rasachk.dailyreportbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PersonalizedDailyReportBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalizedDailyReportBotApplication.class, args);
    }

}
