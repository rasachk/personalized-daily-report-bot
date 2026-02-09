package com.rasachk.dailyreportbot.user.service;

import com.rasachk.dailyreportbot.user.model.TelegramUser;
import com.rasachk.dailyreportbot.user.repository.TelegramUserRepository;
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
    }
}
