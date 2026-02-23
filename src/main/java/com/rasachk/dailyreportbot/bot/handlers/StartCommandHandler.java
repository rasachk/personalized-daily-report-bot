package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;
    private final MainMenuKeyboardFactory mainMenuKeyboardFactory;

    @Override
    public SendMessage handle(Update update) {
        telegramUserService.addNewUser(update.getMessage().getFrom(), update.getMessage().getChatId());

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Welcome! You will receive daily reports. Choose an option:");
        sendMessage.setReplyMarkup(mainMenuKeyboardFactory.generateMainMenuKeyboard());

        return sendMessage;
    }
}
