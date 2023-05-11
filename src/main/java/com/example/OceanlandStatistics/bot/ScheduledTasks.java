package com.example.OceanlandStatistics.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final MyBot telegramBot;

    @Scheduled(fixedRate = 10) // 1 saatte bir
    public void sendRegularData() {
        // Veriyi elde etme
        String data = "Hello World!";

        sendTextMessage(Long.parseLong("1234"), data);
    }
    private void sendTextMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
