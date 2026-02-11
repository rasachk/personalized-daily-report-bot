package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.reminder.model.ReminderType;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import com.rasachk.dailyreportbot.weather.service.WeatherForcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateReminderTypeCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;
    private final WeatherForcastService weatherForcastService;

    @Override
    public SendMessage handle(Update update) {

        ReminderType reminderType = ReminderType.fromTitle(update.getMessage().getText());

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

        Map<String, String> parameters = new HashMap<>();
        parameters.put(Constants.TYPE_KEY, ReminderType.WEATHER_FORCAST.getTitle());

        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_DETAILS, parameters);

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Choose your location: ");

        List<String> cityNames = weatherForcastService.getAvailableCityNames();

        List<KeyboardRow> rows = new ArrayList<>();

        int rowSize = 3;
        for (int i = 0; i < cityNames.size(); i += rowSize) {
            KeyboardRow row = new KeyboardRow();

            for (int j = i; j < i + rowSize && j < cityNames.size(); j++) {
                row.add(new KeyboardButton(cityNames.get(j)));
            }

            rows.add(row);
        }

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(rows);
        keyboard.setResizeKeyboard(true);

        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
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
