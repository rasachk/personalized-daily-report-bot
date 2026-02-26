package com.rasachk.dailyreportbot.user.service;

import com.rasachk.dailyreportbot.user.model.SessionState;
import com.rasachk.dailyreportbot.user.model.TelegramUser;
import com.rasachk.dailyreportbot.user.model.UserSession;
import com.rasachk.dailyreportbot.user.repository.TelegramUserRepository;
import com.rasachk.dailyreportbot.user.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
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
    public void updateUserSessionState(User user, SessionState sessionState, Map<String, String> parameters) {
        TelegramUser telegramUser = telegramUserRepository.findFirstByTelegramId(String.valueOf(user.getId()))
                .orElseThrow(() -> new RuntimeException("TelegramUser Not Found Id: " + user.getId()));

        UserSession userSession = userSessionRepository.findFirstByTelegramUser_Id(telegramUser.getId())
                .orElseThrow(() -> new RuntimeException("UserSession Not Found User Id: " + user.getId()));

        userSession.setSessionState(sessionState);
        userSession.setParameters(parameters);

        userSessionRepository.save(userSession);
    }

    @Override
    public Map<String, String> getUserSessionParameters(User user) {
        TelegramUser telegramUser = telegramUserRepository.findFirstByTelegramId(String.valueOf(user.getId()))
                .orElseThrow(() -> new RuntimeException("TelegramUser Not Found Id: " + user.getId()));

        UserSession userSession = userSessionRepository.findFirstByTelegramUser_Id(telegramUser.getId())
                .orElseThrow(() -> new RuntimeException("UserSession Not Found User Id: " + user.getId()));

        return userSession.getParameters();
    }

    @Override
    public TelegramUser findUserByTelegramId(String telegramId) {
        return telegramUserRepository.findFirstByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("TelegramUser Not Found Id: " + telegramId));
    }
}
