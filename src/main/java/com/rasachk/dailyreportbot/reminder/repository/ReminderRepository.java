package com.rasachk.dailyreportbot.reminder.repository;

import com.rasachk.dailyreportbot.reminder.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByReminderTimeAndIsActiveTrueAndIsDeletedFalse(LocalTime time);
}
