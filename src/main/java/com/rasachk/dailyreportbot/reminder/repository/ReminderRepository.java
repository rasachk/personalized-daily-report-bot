package com.rasachk.dailyreportbot.reminder.repository;

import com.rasachk.dailyreportbot.reminder.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByReminderTimeAndIsActiveTrueAndIsDeletedFalse(LocalTime time);

    List<Reminder> findByTelegramUser_IdAndIsDeletedFalse(Long id);

    Optional<Reminder> findFirstById(Long id);
}
