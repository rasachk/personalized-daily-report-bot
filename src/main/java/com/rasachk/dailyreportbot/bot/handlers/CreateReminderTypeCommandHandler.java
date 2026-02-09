package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.reminder.model.ReminderType;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CreateReminderTypeCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;

    @Override
    public SendMessage handle(Update update) {

        ReminderType reminderType = ReminderType.valueOf(update.getMessage().getText());

        SendMessage sendMessage;
        switch (reminderType) {
            case WEATHER_FORCAST -> sendMessage = handleWeatherForcastTypeButton(update);
            case CURRENCY -> sendMessage = handleCurrencyTypeButton(update);
            case PERSONAL -> sendMessage = handlePersonalTypeButton(update);
            case SPORTS -> sendMessage = handleSportsTypeButton(update);
            default ->
                    sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), Constants.COMMAND_NOT_RECOGNIZED_ERROR);
        }

        return sendMessage;
    }


    private SendMessage handleWeatherForcastTypeButton(Update update) {
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_DETAILS, null);
        return null;
    }


    private SendMessage handleCurrencyTypeButton(Update update) {
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_DETAILS, null);
        return null;
    }


    private SendMessage handlePersonalTypeButton(Update update) {
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_DETAILS, null);
        return null;
    }


    private SendMessage handleSportsTypeButton(Update update) {
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_DETAILS, null);
        return null;
    }
}
