package com.rasachk.dailyreportbot.telegram;

import com.rasachk.dailyreportbot.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
@RequiredArgsConstructor
public class BotRegistrationHandler {

    private final UserService userService;

    @Value("${api.key.telegram}")
    private String telegramApiKey;

    @PostConstruct
    public void init() {
        try {
            log.info("Registering bot...");
            log.info("telegramApiKey: {}", telegramApiKey);
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(telegramApiKey, new DailyReportBot(userService));
        } catch (TelegramApiException telegramApiException) {
            log.error("Error in registering bot", telegramApiException);
        }
    }
}
