package com.rasachk.dailyreportbot;

import com.rasachk.dailyreportbot.telegram.DailyReportBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@Slf4j
public class PersonalizedDailyReportBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalizedDailyReportBotApplication.class, args);

        try {
            log.info("Registering bot...");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new DailyReportBot());
        } catch (TelegramApiException telegramApiException) {
            log.error("Error in registering bot", telegramApiException);
        }
    }

}
