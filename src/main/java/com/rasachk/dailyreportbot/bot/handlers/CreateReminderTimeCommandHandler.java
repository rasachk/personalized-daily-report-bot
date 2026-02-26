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

import java.time.LocalTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateReminderTimeCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;
    private final ReminderService reminderService;
    private final MainMenuKeyboardFactory mainMenuKeyboardFactory;

    @Override
    public SendMessage handle(Update update) {

        Map<String, String> sessionParameters = telegramUserService.getUserSessionParameters(update.getMessage().getFrom());

        //TODO VALIDATE TIME TEXT
        LocalTime reminderTime = LocalTime.parse(update.getMessage().getText());
        ReminderType reminderType = ReminderType.fromTitle(sessionParameters.remove(Constants.TYPE_KEY));

        reminderService.createNewReminder(reminderType, reminderTime, sessionParameters, update.getMessage().getFrom());

        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.MAIN_MENU, null);

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Reminder created successfully!");
        sendMessage.setReplyMarkup(mainMenuKeyboardFactory.generateMainMenuKeyboard());

        return sendMessage;
    }
}
