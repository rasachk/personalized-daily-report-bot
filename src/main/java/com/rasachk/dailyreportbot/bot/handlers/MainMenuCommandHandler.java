package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.reminder.model.ReminderType;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MainMenuCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;


    @Override
    public SendMessage handle(Update update) {

        SendMessage sendMessage;
        switch (update.getMessage().getText()) {
            case Constants.CREATE_NEW_REMINDER_BUTTON -> sendMessage = handleCreateNewReminderButton(update);
            case Constants.MANAGE_REMINDERS_BUTTON -> sendMessage = handleManageRemindersButton(update);
            case Constants.FAQ_BUTTON ->
                    sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), Constants.FAQ_MESSAGE);
            case Constants.SUPPORT_BUTTON ->
                    sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), Constants.SUPPORT_MESSAGE);
            default ->
                    sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), Constants.COMMAND_NOT_RECOGNIZED_ERROR);
        }

        return sendMessage;
    }


    @Nullable
    private SendMessage handleManageRemindersButton(Update update) {
        SendMessage sendMessage = null;
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.MANAGE_REMINDERS, null);
        return sendMessage;
    }


    private SendMessage handleCreateNewReminderButton(Update update) {

        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_TYPE, null);

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Choose the reminder type you want to create:");

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ReminderType.WEATHER_FORCAST.getTitle()));
        row1.add(new KeyboardButton(ReminderType.CURRENCY.getTitle()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ReminderType.PERSONAL.getTitle()));
        row2.add(new KeyboardButton(ReminderType.SPORTS.getTitle()));

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(List.of(row1, row2));
        keyboard.setResizeKeyboard(true);

        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }
}
