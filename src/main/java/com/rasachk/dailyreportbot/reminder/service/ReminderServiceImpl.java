package com.rasachk.dailyreportbot.reminder.service;

import com.rasachk.dailyreportbot.reminder.model.Reminder;
import com.rasachk.dailyreportbot.reminder.model.ReminderType;
import com.rasachk.dailyreportbot.reminder.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;

    @Override
    public void createNewReminder(ReminderType reminderType, LocalTime reminderTime, Map<String, String> parameters) {

        Reminder reminder = Reminder.builder()
                .reminderType(reminderType)
                .reminderTime(reminderTime)
                .parameters(parameters)
                .isActive(true)
                .isDeleted(false)
                .build();

        reminderRepository.save(reminder);
    }
}
