package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.reminder.model.ReminderType;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateReminderDetailsCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;

    @Override
    public SendMessage handle(Update update) {
        Map<String, String> parameters = telegramUserService.getUserSessionParameters(update.getMessage().getFrom());

        ReminderType reminderType = ReminderType.fromTitle(parameters.get(Constants.TYPE_KEY));

        SendMessage sendMessage;
        switch (reminderType) {
            case WEATHER_FORECAST -> sendMessage = handleWeatherForcastDetailButton(update, parameters);
            case CURRENCY -> sendMessage = handleCurrencyDetailButton(update, parameters);
            case PERSONAL -> sendMessage = handlePersonalDetailButton(update, parameters);
//            case SPORTS -> sendMessage = handleSportsTypeButton(update);
            default ->
                    sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), Constants.COMMAND_NOT_RECOGNIZED_ERROR);
        }

        return sendMessage;
    }


    private SendMessage handleCurrencyDetailButton(Update update, Map<String, String> parameters) {
        //TODO VALIDATE CURRENCY
        parameters.put(Constants.CURRENCY_KEY, update.getMessage().getText());
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_TIME, parameters);
        return generateReminderTimeSendMessage(update.getMessage().getChatId());
    }


    private SendMessage handlePersonalDetailButton(Update update, Map<String, String> parameters) {
        parameters.put(Constants.PERSONAL_MESSAGE_KEY, update.getMessage().getText());
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_TIME, parameters);
        return generateReminderTimeSendMessage(update.getMessage().getChatId());
    }


    private SendMessage handleWeatherForcastDetailButton(Update update, Map<String, String> parameters) {
        //TODO VALIDATE LOCATION
        parameters.put(Constants.LOCATION_KEY, update.getMessage().getText());
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_TIME, parameters);
        return generateReminderTimeSendMessage(update.getMessage().getChatId());
    }


    private SendMessage generateReminderTimeSendMessage(Long chatId) {

        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "Choose your daily reminder time:");

        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow currentRow = new KeyboardRow();

        for (int hour = 0; hour < 24; hour++) {
            for (int minute : new int[]{0, 30}) {

                String time = String.format("%02d:%02d", hour, minute);
                currentRow.add(new KeyboardButton(time));

                if (currentRow.size() == 4) {
                    rows.add(currentRow);
                    currentRow = new KeyboardRow();
                }
            }
        }

        if (!currentRow.isEmpty()) {
            rows.add(currentRow);
        }

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(rows);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);

        sendMessage.setReplyMarkup(keyboard);

        return sendMessage;
    }
}
