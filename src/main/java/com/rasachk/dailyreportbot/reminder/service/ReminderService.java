package com.rasachk.dailyreportbot.reminder.service;

import com.rasachk.dailyreportbot.reminder.model.Reminder;
import com.rasachk.dailyreportbot.reminder.model.ReminderType;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface ReminderService {
    void createNewReminder(ReminderType reminderType, LocalTime reminderTime, Map<String, String> parameters, User user);

    List<Reminder> findScheduledReminders(LocalTime localTime);

    List<Reminder> getUserReminderList(User user);

    void changeReminderActivationStatus(Long reminderId, Boolean newStatus);

    void deleteReminder(Long reminderId);
}
