package com.rasachk.dailyreportbot.reminder.service;

import com.rasachk.dailyreportbot.reminder.model.Reminder;
import com.rasachk.dailyreportbot.reminder.model.ReminderType;
import com.rasachk.dailyreportbot.reminder.repository.ReminderRepository;
import com.rasachk.dailyreportbot.user.model.TelegramUser;
import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final TelegramUserService telegramUserService;

    @Override
    public void createNewReminder(ReminderType reminderType, LocalTime reminderTime, Map<String, String> parameters, User user) {

        Reminder reminder = Reminder.builder()
                .reminderType(reminderType)
                .reminderTime(reminderTime)
                .parameters(parameters)
                .telegramUser(telegramUserService.findUserByTelegramId(String.valueOf(user.getId())))
                .isActive(true)
                .isDeleted(false)
                .build();

        reminderRepository.save(reminder);
    }

    @Override
    public List<Reminder> findScheduledReminders(LocalTime localTime) {
        return reminderRepository.findByReminderTimeAndIsActiveTrueAndIsDeletedFalse(localTime);
    }

    @Override
    public List<Reminder> getUserReminderList(User user) {
        TelegramUser telegramUser = telegramUserService.findUserByTelegramId(String.valueOf(user.getId()));
        return reminderRepository.findByTelegramUser_IdAndIsDeletedFalse(telegramUser.getId());
    }

    @Override
    public void changeReminderActivationStatus(Long reminderId, Boolean newStatus) {
        Reminder reminder = reminderRepository.findFirstById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder Not Found Id: " + reminderId));

        reminder.setIsActive(newStatus);

        reminderRepository.save(reminder);
    }

    @Override
    public void deleteReminder(Long reminderId) {
        Reminder reminder = reminderRepository.findFirstById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder Not Found Id: " + reminderId));

        reminder.setIsDeleted(true);

        reminderRepository.save(reminder);
    }
}
