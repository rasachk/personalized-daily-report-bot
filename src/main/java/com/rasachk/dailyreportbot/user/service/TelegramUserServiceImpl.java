package com.rasachk.dailyreportbot.user.service;

import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.model.TelegramUser;
import com.rasachk.dailyreportbot.user.model.UserSession;
import com.rasachk.dailyreportbot.user.repository.TelegramUserRepository;
import com.rasachk.dailyreportbot.user.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;
    private final UserSessionRepository userSessionRepository;


    @Override
    public void addNewUser(User user, Long chatId) {

        Optional<TelegramUser> telegramUserOptional = telegramUserRepository.findFirstByTelegramId(String.valueOf(user.getId()));

        TelegramUser telegramUser;
        if (telegramUserOptional.isEmpty()) {
            telegramUser = TelegramUser.builder()
                    .telegramId(String.valueOf(user.getId()))
                    .username(user.getUserName())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .chatId(String.valueOf(chatId))
                    .build();

            log.info("Adding new telegram user: {}", telegramUser);

        } else {
            telegramUser = telegramUserOptional.get();
            telegramUser.setChatId(String.valueOf(chatId));

            log.info("Updating telegram user: {}", telegramUser);
        }

        telegramUserRepository.save(telegramUser);

        Optional<UserSession> userSessionOptional = userSessionRepository.findFirstByTelegramUser_Id(telegramUser.getId());

        UserSession userSession;
        if (userSessionOptional.isEmpty()) {
            userSession = UserSession.builder()
                    .telegramUser(telegramUser)
                    .sessionState(SessionState.MAIN_MENU)
                    .build();
        } else {
            userSession = userSessionOptional.get();
            userSession.setSessionState(SessionState.MAIN_MENU);
            userSession.setParameters(null);
        }

        userSessionRepository.save(userSession);
    }

    @Override
    public SessionState getUserSessionState(User user) {

        TelegramUser telegramUser = telegramUserRepository.findFirstByTelegramId(String.valueOf(user.getId()))
                .orElseThrow(() -> new RuntimeException("TelegramUser Not Found Id: " + user.getId()));

        UserSession userSession = userSessionRepository.findFirstByTelegramUser_Id(telegramUser.getId())
                .orElseThrow(() -> new RuntimeException("UserSession Not Found User Id: " + user.getId()));

        return userSession.getSessionState();
    }

    @Override
    public void updateUserSessionState(User user, SessionState sessionState, String parameters) {
        TelegramUser telegramUser = telegramUserRepository.findFirstByTelegramId(String.valueOf(user.getId()))
                .orElseThrow(() -> new RuntimeException("TelegramUser Not Found Id: " + user.getId()));

        UserSession userSession = userSessionRepository.findFirstByTelegramUser_Id(telegramUser.getId())
                .orElseThrow(() -> new RuntimeException("UserSession Not Found User Id: " + user.getId()));

        userSession.setSessionState(sessionState);
        userSession.setParameters(parameters);

        userSessionRepository.save(userSession);
    }
}
