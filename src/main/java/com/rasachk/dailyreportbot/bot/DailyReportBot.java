package com.rasachk.dailyreportbot.bot;

import com.rasachk.dailyreportbot.bot.handlers.*;
import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.reminder.model.Reminder;
import com.rasachk.dailyreportbot.reminder.service.ReminderService;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import com.rasachk.dailyreportbot.weather.service.WeatherForecastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DailyReportBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramUserService telegramUserService;
    private final StartCommandHandler startCommandHandler;
    private final MainMenuCommandHandler mainMenuCommandHandler;
    private final CreateReminderTypeCommandHandler createReminderTypeCommandHandler;
    private final CreateReminderDetailsCommandHandler createReminderDetailsCommandHandler;
    private final CreateReminderTimeCommandHandler createReminderTimeCommandHandler;
    private final ManageRemindersCommandHandler manageRemindersCommandHandler;
    private final ReminderService reminderService;
    private final WeatherForecastService weatherForecastService;
    private final TelegramClient telegramClient = new OkHttpTelegramClient("token");

    @Override
    public void consume(Update update) {
        try {

            if (update.hasMessage() && update.getMessage().hasText()) {

                log.info("Received update from userId {} text: {}", update.getMessage().getFrom().getId(), update.getMessage().getText());

                SendMessage sendMessage;
                if (update.getMessage().getText().equals("/start")) {
                    sendMessage = startCommandHandler.handle(update);
                } else {
                    SessionState sessionState = telegramUserService.getUserSessionState(update.getMessage().getFrom());
                    sendMessage = switch (sessionState) {
                        case MAIN_MENU -> mainMenuCommandHandler.handle(update);
                        case CREATE_REMINDER_TYPE -> createReminderTypeCommandHandler.handle(update);
                        case CREATE_REMINDER_DETAILS -> createReminderDetailsCommandHandler.handle(update);
                        case CREATE_REMINDER_TIME -> createReminderTimeCommandHandler.handle(update);
                        case MANAGE_REMINDERS -> manageRemindersCommandHandler.handle(update);
                    };
                }

                log.info("Sending message to user: {}", sendMessage);

                telegramClient.execute(sendMessage);
            }
        } catch (TelegramApiException telegramApiException) {
            log.error("Error in onUpdateReceived", telegramApiException);
        }

    }

    public void executeMessageToUser(SendMessage sendMessage) {
        try {
            log.info("Sending message to user: {}", sendMessage);
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException telegramApiException) {
            log.error("Error in onUpdateReceived", telegramApiException);
        }
    }


    @Scheduled(cron = "0 */30 * * * *")
    public void processReminders() {

        LocalTime now = LocalTime.now()
                .withSecond(0)
                .withNano(0);

        log.info("Checking reminders for {}", now);

        List<Reminder> reminderList = reminderService.findScheduledReminders(now);

        log.info("ReminderList size: {}", reminderList.size());

        List<SendMessage> sendMessageList = new ArrayList<>();
        for (Reminder reminder : reminderList) {
            String message = switch (reminder.getReminderType()) {
                case WEATHER_FORECAST -> weatherForecastService.getWeatherForcastMessage(reminder.getParameters());
                case CURRENCY -> null;
                case PERSONAL ->
                        "Your personal daily reminder:\n" + reminder.getParameters().get(Constants.PERSONAL_MESSAGE_KEY);
                case SPORTS -> null;
            };

            sendMessageList.add(new SendMessage(reminder.getTelegramUser().getChatId(), message));
        }

        for (SendMessage sendMessage : sendMessageList) {
            executeMessageToUser(sendMessage);
        }

    }


}
