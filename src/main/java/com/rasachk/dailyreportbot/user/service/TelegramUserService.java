package com.rasachk.dailyreportbot.user.service;

import org.telegram.telegrambots.meta.api.objects.User;

public interface TelegramUserService {
    void addNewUser(User user, Long chatId);

}
