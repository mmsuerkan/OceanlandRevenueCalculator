package com.example.OceanlandStatistics.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {


    private long chat_id;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            chat_id = update.getMessage().getChatId();

            // Burada, belirli bir mesaj alındığında API çağrısını başlatma ve sonucu gönderme işlemi başlatılır.
            if (message_text.equals("/start")) {
                String apiResult = calculateRevenue();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chat_id);
                sendMessage.setText(apiResult);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.equals("/show")){
                String apiResult = showCurrentValues();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chat_id);
                sendMessage.setText(apiResult);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String showCurrentValues() {
        ApiController apiController = new ApiController();
        return "Oland " + apiController.getOlandPrice() + "\n" +
                "OWater " + apiController.getWaterPrice() + "\n" +
                "OFood " + apiController.getFoodPrice() + "\n" +
                "OWood " + apiController.getWoodPrice() + "\n" +
                "OMetal " + apiController.getMetalPrice() + "\n" ;
    }

    private String calculateRevenue() {
        ApiController apiController = new ApiController();
        return apiController.calculateDailyRevenue().toString();
    }

    @Override
    public String getBotUsername() {
        // Botunuzun kullanıcı adını döndürün
        return "CryptoConveksBot";
    }

    @Override
    public String getBotToken() {
        // Botunuzun token'ını döndürün
        return "5886872975:AAGEMC5hF9PQDEQe9_jBfy_RSHSwo1nwiFk";
    }
}