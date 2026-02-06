package com.rasachk.dailyreportbot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
public class DailyReportBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "PersonalDailyReportHelperBot";
    }

    @Override
    public String getBotToken() {
        return "botToken";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {

            if (update.hasMessage() && update.getMessage().hasText()) {

                update.getMessage().getFrom().getId();

                String text = update.getMessage().getText();
                Long chatId = update.getMessage().getChatId();

                SendMessage message = new SendMessage();
                message.setChatId(chatId.toString());

                if (text.equals("/start")) {
                    message.setText("Welcome! You will receive daily reports. Choose an option:");

                    ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

                    KeyboardRow row1 = new KeyboardRow();
                    row1.add(new KeyboardButton("Weather"));
                    row1.add(new KeyboardButton("News"));

                    KeyboardRow row2 = new KeyboardRow();
                    row2.add(new KeyboardButton("Markets"));
                    row2.add(new KeyboardButton("Reminders"));

                    keyboard.setKeyboard(List.of(row1, row2));
                    keyboard.setResizeKeyboard(true);

                    message.setReplyMarkup(keyboard);

                    execute(message);

                } else {
                    message.setText("Command not recognized.");
                }

                execute(message);
            }
        } catch (TelegramApiException telegramApiException) {
            log.error("Error in onUpdateReceived", telegramApiException);
        }

    }
}
