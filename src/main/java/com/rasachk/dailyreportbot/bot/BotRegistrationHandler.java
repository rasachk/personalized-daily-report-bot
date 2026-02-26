package com.rasachk.dailyreportbot.bot;

import com.rasachk.dailyreportbot.bot.handlers.*;
import com.rasachk.dailyreportbot.currency.service.CurrencyService;
import com.rasachk.dailyreportbot.reminder.service.ReminderService;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import com.rasachk.dailyreportbot.weather.service.WeatherForecastService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Log4j2
@RequiredArgsConstructor
public class BotRegistrationHandler {

    private final TelegramUserService telegramUserService;
    private final StartCommandHandler startCommandHandler;
    private final MainMenuCommandHandler mainMenuCommandHandler;
    private final CreateReminderTypeCommandHandler createReminderTypeCommandHandler;
    private final CreateReminderDetailsCommandHandler createReminderDetailsCommandHandler;
    private final CreateReminderTimeCommandHandler createReminderTimeCommandHandler;
    private final ManageRemindersCommandHandler manageRemindersCommandHandler;
    private final ReminderService reminderService;
    private final WeatherForecastService weatherForecastService;
    private final CurrencyService currencyService;

    @Value("${api.key.telegram}")
    private String telegramApiKey;

    @PostConstruct
    public void init() {
        try {
            log.info("Registering bot...");

            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();

            botsApplication.registerBot(telegramApiKey,
                    new DailyReportBot(
                            telegramUserService,
                            startCommandHandler,
                            mainMenuCommandHandler,
                            createReminderTypeCommandHandler,
                            createReminderDetailsCommandHandler,
                            createReminderTimeCommandHandler,
                            manageRemindersCommandHandler,
                            reminderService,
                            weatherForecastService,
                            currencyService));

        } catch (TelegramApiException telegramApiException) {
            log.error("Error in registering bot", telegramApiException);
        }
    }
}
