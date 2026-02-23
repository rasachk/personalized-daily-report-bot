package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class MainMenuKeyboardFactory {
    public ReplyKeyboardMarkup generateMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Constants.CREATE_NEW_REMINDER_BUTTON));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Constants.MANAGE_REMINDERS_BUTTON));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(Constants.FAQ_BUTTON));
        row3.add(new KeyboardButton(Constants.SUPPORT_BUTTON));

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(List.of(row1, row2, row3));
        keyboard.setResizeKeyboard(true);
        return keyboard;
    }
}
