package com.rasachk.dailyreportbot.bot.handlers;

import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CreateReminderDetailsCommandHandler implements CommandHandler {

    private final TelegramUserService telegramUserService;

    @Override
    public SendMessage handle(Update update) {
        return null;
    }
}
