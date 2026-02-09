package com.rasachk.dailyreportbot.user.repository;

import com.rasachk.dailyreportbot.user.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findFirstByTelegramUser_Id(Long id);

}
