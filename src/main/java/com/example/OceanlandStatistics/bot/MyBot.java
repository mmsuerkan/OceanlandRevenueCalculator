package com.example.OceanlandStatistics.bot;

import com.example.OceanlandStatistics.PriceRepository;
import com.example.OceanlandStatistics.PriceService;
import com.example.OceanlandStatistics.model.NftStats;
import com.example.OceanlandStatistics.model.Price;
import com.example.OceanlandStatistics.model.ResourceStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DecimalFormat;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {


    private long chat_id;

    @Autowired
    private PriceService priceService;

    @Autowired
    private PriceRepository priceRepository;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            chat_id = update.getMessage().getChatId();

            // Burada, belirli bir mesaj alındığında API çağrısını başlatma ve sonucu gönderme işlemi başlatılır.
            if (message_text.equals("/start")) {
                ApiController apiController = new ApiController(priceService);
                String apiResult = calculateRevenue(apiController);

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
            } else if (message_text.startsWith("/set")) {
                ApiController apiController = new ApiController(priceService);
                String[] splitMessage = message_text.split(" ");
                String command = splitMessage[0];
                double price = Double.parseDouble(splitMessage[1]);

                String savingResult = "";

                switch (command) {
                    case "/setoland":
                        savingResult = priceService.saveOlandPrice(price);
                        break;
                    case "/setwater":
                        savingResult = priceService.saveWaterPrice(price);
                        break;
                    case "/setfood":
                        savingResult = priceService.saveFoodPrice(price);
                        break;
                    case "/setwood":
                        savingResult = priceService.saveWoodPrice(price);
                        break;
                    case "/setmetal":
                        savingResult = priceService.saveMetalPrice(price);
                        break;

                    default:
                        savingResult = "Invalid command";
                }

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chat_id);
                sendMessage.setText("Save is : " + savingResult);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.equals("/resources")){
                ApiController apiController = new ApiController(priceService);
                ResourceStats resourceStats = apiController.calculateHourlyResourceStats();

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chat_id);
                sendMessage.setText(resourceStats.toString());
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


            }else if (message_text.equals("/nfts")){
                ApiController apiController = new ApiController(priceService);
                List<NftStats> nftStats = apiController.calculateNftStats();
                String result = "";
                for (NftStats nftStat : nftStats) {
                    result += nftStat.toString() + "\n";
                }
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chat_id);
                sendMessage.setText(result);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.equals("/craftcost")){
                ApiController apiController = new ApiController(priceService);
                CraftCost craftCost = apiController.initialCraftCost();
                DecimalFormat df = new DecimalFormat("#.##");
                String result = "Total needed Oland for 60 nfts: " + df.format(craftCost.getTotalNeededOland()) + " Oland"+ "\n" +
                                "Total needed USDT for 60 nfts: " + df.format(craftCost.getTotalNeededUsdt()) + " USDT" + "\n";

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chat_id);
                sendMessage.setText(result);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if (message_text.equals("/currentbalance")){
                ApiController apiController = new ApiController(priceService);
                Balance balance = apiController.currentBalance();
                String result =  "Current Total Oland: " +balance.getTotalOland() + "\nCurrent Total Usdt: " + balance.getTotalUsdt() ;

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chat_id);
                sendMessage.setText(result);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String showCurrentValues() {
        Price lastPrices = getLastPrices();
        if (lastPrices != null) {
            return "Oland " + lastPrices.getOlandPrice() + "\n" +
                    "OWater " + lastPrices.getWaterPrice() + "\n" +
                    "OFood " + lastPrices.getFoodPrice() + "\n" +
                    "OWood " + lastPrices.getWoodPrice() + "\n" +
                    "OMetal " + lastPrices.getMetalPrice() + "\n";
        }
        return "No prices available";
    }
    private Price getLastPrices() {
        try {
            return priceRepository.findTopByOrderByIdDesc();
        } catch (Exception e) {
            return null;
        }
    }

    private String calculateRevenue(ApiController apiController) {
        return apiController.calculateDailyRevenue().toString();
    }

    @Override
    public String getBotUsername() {
        // Botunuzun kullanıcı adını döndürün
        return "mmerts_bot";
    }

    @Override
    public String getBotToken() {
        // Botunuzun token'ını döndürün
        return "5948896804:AAHDmvSL24MQ42tKUJsdElzM-2QKOGzx98o";
    }
}