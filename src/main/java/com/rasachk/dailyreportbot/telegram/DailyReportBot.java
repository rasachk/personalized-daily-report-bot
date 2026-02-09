package com.rasachk.dailyreportbot.telegram;

import com.rasachk.dailyreportbot.user.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DailyReportBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramUserService telegramUserService;

    //    @Value("${api.key.telegram}")
//    private String telegramApiKey;
    private TelegramClient telegramClient = new OkHttpTelegramClient("token");

    @Override
    public void consume(Update update) {
        try {

            if (update.hasMessage() && update.getMessage().hasText()) {

                log.info("Received update from userId {} text: {}", update.getMessage().getFrom().getId(), update.getMessage().getText());

                SendMessage sendMessage;
                if (update.getMessage().getText().equals("/start")) {
                    sendMessage = handleNewUser(update);

                } else {
                    sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Command not recognized.");
                }

                log.info("Sending message to user: {}", sendMessage);

                telegramClient.execute(sendMessage);
            }
        } catch (TelegramApiException telegramApiException) {
            log.error("Error in onUpdateReceived", telegramApiException);
        }

    }

    @NotNull
    private SendMessage handleNewUser(Update update) {

        telegramUserService.addNewUser(update.getMessage().getFrom(),update.getMessage().getChatId());

        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Welcome! You will receive daily reports. Choose an option:");

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Create New Report"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Manage Reports"));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("FAQ"));
        row3.add(new KeyboardButton("Support"));

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(List.of(row1, row2, row3));
        keyboard.setResizeKeyboard(true);

        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }
}
