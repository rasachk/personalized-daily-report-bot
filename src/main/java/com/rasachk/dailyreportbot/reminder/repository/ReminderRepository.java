package com.rasachk.dailyreportbot.reminder.repository;

import com.rasachk.dailyreportbot.reminder.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

}
