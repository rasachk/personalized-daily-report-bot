package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class MainMenuCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;

    @Override
    public SendMessage handle(Update update) {

        SendMessage sendMessage;
        switch (update.getMessage().getText()) {
            case Constants.CREATE_NEW_REMINDER_BUTTON -> {
                telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.CREATE_REMINDER_TYPE, null);
                sendMessage = null;
            }
            case Constants.MANAGE_REMINDERS_BUTTON -> {
                telegramUserService.updateUserSessionState(update.getMessage().getFrom(), SessionState.MANAGE_REMINDERS, null);
                sendMessage = null;
            }
            case Constants.FAQ_BUTTON ->
                    sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), Constants.FAQ_MESSAGE);
            case Constants.SUPPORT_BUTTON ->
                    sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), Constants.SUPPORT_MESSAGE);
            default ->
                    sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), Constants.COMMAND_NOT_RECOGNIZED_ERROR);
        }

        return sendMessage;
    }
}
