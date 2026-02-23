package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.reminder.model.Reminder;
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

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MainMenuCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;
    private final ReminderService reminderService;


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


    private SendMessage handleManageRemindersButton(Update update) {
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.MANAGE_REMINDERS_MENU, null);
        List<Reminder> reminderList = reminderService.getUserReminderList(update.getMessage().getFrom());
        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), generateManageRemindersMessage(reminderList));
        sendMessage.setReplyMarkup(generateManageRemindersKeyboard(reminderList));
        return sendMessage;
    }

    private ReplyKeyboardMarkup generateManageRemindersKeyboard(List<Reminder> remindersList) {
        List<KeyboardRow> rows = new ArrayList<>();

        int index = 1;

        for (Reminder reminder : remindersList) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(index + ". " + (reminder.getIsActive() ? Constants.DEACTIVATE_BUTTON : Constants.ACTIVATE_BUTTON)));
            row.add(new KeyboardButton(index + ". " + Constants.DELETE_BUTTON));
            index++;
            rows.add(row);
        }

        rows.add(new KeyboardRow(new KeyboardButton(Constants.BACK_BUTTON)));

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(rows);
        keyboard.setResizeKeyboard(true);

        return keyboard;

    }


    public String generateManageRemindersMessage(List<Reminder> reminders) {
        if (reminders == null || reminders.isEmpty()) {
            return "📭 You have no reminders set.";
        }

        StringBuilder message = new StringBuilder();
        message.append("⏰ *Your Reminders*\n\n");

        int index = 1;

        for (Reminder reminder : reminders) {
            message.append(index++).append(". ");

            message.append(reminder.getReminderType().getTitle()).append("\n");
            message.append("Time: ").append(reminder.getReminderTime()).append("\n");

            if (reminder.getParameters() != null && !reminder.getParameters().isEmpty()) {
                message.append("Details:").append("\n");
                reminder.getParameters().forEach((key, value) ->
                        message.append(key).append(": ").append(value).append("\n")
                );
            }

            message.append("Status: ").append(Boolean.TRUE.equals(reminder.getIsActive()) ? "Active ✅" : "Inactive ❌").append("\n");

            message.append("\n");
        }

        message.append("\n").append("Choose the reminder you want to modify:");

        return message.toString();
    }


    private SendMessage handleCreateNewReminderButton(Update update) {

        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_TYPE, null);

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Choose the reminder type you want to create:");

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ReminderType.WEATHER_FORECAST.getTitle()));
        row1.add(new KeyboardButton(ReminderType.CURRENCY.getTitle()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ReminderType.PERSONAL.getTitle()));
        row2.add(new KeyboardButton(ReminderType.SPORTS.getTitle()));

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(List.of(row1, row2));
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);

        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }
}
