package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.reminder.model.ReminderType;
import com.rasachk.dailyreportbot.reminder.service.ReminderService;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateReminderTimeCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;
    private final ReminderService reminderService;

    @Override
    public SendMessage handle(Update update) {

        Map<String, String> sessionParameters = telegramUserService.getUserSessionParameters(update.getMessage().getFrom());

        //TODO VALIDATE TIME TEXT
        LocalTime reminderTime = LocalTime.parse(update.getMessage().getText());

        ReminderType reminderType = ReminderType.fromTitle(sessionParameters.get(Constants.TYPE_KEY));

        reminderService.createNewReminder(reminderType, reminderTime, sessionParameters);

        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.MAIN_MENU, null);

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Reminder created successfully!");

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Constants.CREATE_NEW_REMINDER_BUTTON));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Constants.MANAGE_REMINDERS_BUTTON));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(Constants.FAQ_BUTTON));
        row3.add(new KeyboardButton(Constants.SUPPORT_BUTTON));

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(List.of(row1, row2, row3));
        keyboard.setResizeKeyboard(true);

        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }
}
