package com.rasachk.dailyreportbot.reminder.service;

import com.rasachk.dailyreportbot.reminder.model.ReminderType;

import java.time.LocalTime;
import java.util.Map;

public interface ReminderService {
    void createNewReminder(ReminderType reminderType, LocalTime reminderTime, Map<String, String> parameters);
}
