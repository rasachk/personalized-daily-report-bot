package com.rasachk.dailyreportbot.user.service;

import com.rasachk.dailyreportbot.user.model.SessionState;
import org.telegram.telegrambots.meta.api.objects.User;

public interface TelegramUserService {

    void addNewUser(User user, Long chatId);

    SessionState getUserSessionState(User user);

    void updateUserSessionState(User user, SessionState sessionState, String parameters);

}
