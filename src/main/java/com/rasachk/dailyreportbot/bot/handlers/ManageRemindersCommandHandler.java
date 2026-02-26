package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.reminder.model.Reminder;
import com.rasachk.dailyreportbot.reminder.service.ReminderService;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ManageRemindersCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;
    private final ReminderService reminderService;
    private final MainMenuKeyboardFactory mainMenuKeyboardFactory;

    @Override
    public SendMessage handle(Update update) {
        SendMessage sendMessage;
        if (update.getMessage().getText().equals(Constants.BACK_BUTTON)) {
            sendMessage = handleBackButton(update);
        } else {
            List<Reminder> reminderList = reminderService.getUserReminderList(update.getMessage().getFrom());

            Pattern pattern = Pattern.compile("(\\d+)\\.\\s*(\\w+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(update.getMessage().getText().trim());

            if (matcher.matches()) {
                Integer index = Integer.parseInt(matcher.group(1));
                String action = matcher.group(2);

                switch (action) {
                    case Constants.ACTIVATE_BUTTON ->
                            reminderService.changeReminderActivationStatus(reminderList.get(index - 1).getId(), true);
                    case Constants.DEACTIVATE_BUTTON ->
                            reminderService.changeReminderActivationStatus(reminderList.get(index - 1).getId(), false);
                    case Constants.DELETE_BUTTON -> reminderService.deleteReminder(reminderList.get(index - 1).getId());
                    default -> throw new RuntimeException(Constants.COMMAND_NOT_RECOGNIZED_ERROR);
                }

                telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.MAIN_MENU, null);
                sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Reminder updated successfully!\nChoose an option:");
                sendMessage.setReplyMarkup(mainMenuKeyboardFactory.generateMainMenuKeyboard());

            } else throw new RuntimeException(Constants.COMMAND_NOT_RECOGNIZED_ERROR);
        }


        return sendMessage;
    }

    private SendMessage handleBackButton(Update update) {
        telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.MAIN_MENU, null);

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Choose an option:");
        sendMessage.setReplyMarkup(mainMenuKeyboardFactory.generateMainMenuKeyboard());

        return sendMessage;
    }
}
