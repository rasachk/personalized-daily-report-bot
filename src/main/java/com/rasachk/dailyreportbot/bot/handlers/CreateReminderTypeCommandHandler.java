package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.currency.service.CurrencyService;
import com.rasachk.dailyreportbot.reminder.model.ReminderType;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import com.rasachk.dailyreportbot.weather.service.WeatherForecastService;
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
    private final WeatherForecastService weatherForecastService;
    private final CurrencyService currencyService;

    @Override
    public SendMessage handle(Update update) {

        ReminderType reminderType = ReminderType.fromTitle(update.getMessage().getText());

        SendMessage sendMessage;
        switch (reminderType) {
            case WEATHER_FORECAST -> sendMessage = handleWeatherForcastTypeButton(update);
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
        parameters.put(Constants.TYPE_KEY, ReminderType.WEATHER_FORECAST.getTitle());

        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_DETAILS, parameters);

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Choose your location: ");

        List<String> cityNames = weatherForecastService.getAvailableCityNames();

        sendMessage.setReplyMarkup(generateReplyKeyboardMarkup(cityNames));
        return sendMessage;
    }


    private SendMessage handleCurrencyTypeButton(Update update) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(Constants.TYPE_KEY, ReminderType.CURRENCY.getTitle());

        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_DETAILS, parameters);

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Choose your currency: ");

        List<String> cityNames = currencyService.getAvailableCurrencyNames();

        sendMessage.setReplyMarkup(generateReplyKeyboardMarkup(cityNames));
        return sendMessage;
    }


    private SendMessage handlePersonalTypeButton(Update update) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(Constants.TYPE_KEY, ReminderType.PERSONAL.getTitle());
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_DETAILS, parameters);
        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Enter your personal daily reminder message: ");
        //TODO MAKE THE PREVIOUS REPLY KEYBOARD GO AWAY
        return sendMessage;
    }


    private SendMessage handleSportsTypeButton(Update update) {
//        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_DETAILS, null);
        return new SendMessage(String.valueOf(update.getMessage().getChatId()), Constants.COMING_SOON_MESSAGE);
    }

    private ReplyKeyboardMarkup generateReplyKeyboardMarkup(List<String> options) {
        List<KeyboardRow> rows = new ArrayList<>();

        int rowSize = 3;
        for (int i = 0; i < options.size(); i += rowSize) {
            KeyboardRow row = new KeyboardRow();

            for (int j = i; j < i + rowSize && j < options.size(); j++) {
                row.add(new KeyboardButton(options.get(j)));
            }

            rows.add(row);
        }

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(rows);
        keyboard.setResizeKeyboard(true);

        return keyboard;
    }
}
