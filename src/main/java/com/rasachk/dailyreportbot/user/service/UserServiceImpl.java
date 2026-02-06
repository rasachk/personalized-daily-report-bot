package com.rasachk.dailyreportbot.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Override
    public void addNewUser(User telegramUser) {
        log.info("Adding new user");
    }
}
