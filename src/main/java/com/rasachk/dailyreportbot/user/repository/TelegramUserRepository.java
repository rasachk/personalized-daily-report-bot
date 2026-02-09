package com.rasachk.dailyreportbot.user.repository;

import com.rasachk.dailyreportbot.user.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    Optional<TelegramUser> findFirstByTelegramId(String telegramId);

}
