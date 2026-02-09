package com.rasachk.dailyreportbot.bot;

import com.rasachk.dailyreportbot.bot.handlers.*;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
public class DailyReportBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramUserService telegramUserService;
    private final StartCommandHandler startCommandHandler;
    private final MainMenuCommandHandler mainMenuCommandHandler;
    private final CreateReminderTypeCommandHandler createReminderTypeCommandHandler;
    private final CreateReminderDetailsCommandHandler createReminderDetailsCommandHandler;
    private final CreateReminderTimeCommandHandler createReminderTimeCommandHandler;
    private final ManageRemindersCommandHandler manageRemindersCommandHandler;

    //    @Value("${api.key.telegram}")
//    private String telegramApiKey;
    private TelegramClient telegramClient = new OkHttpTelegramClient("token");

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

}
