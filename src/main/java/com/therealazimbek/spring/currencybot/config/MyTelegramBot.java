package com.therealazimbek.spring.currencybot.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.therealazimbek.spring.currencybot.model.TelegramUpdate;
import com.therealazimbek.spring.currencybot.service.CurrentRateFromApi;
import com.therealazimbek.spring.currencybot.service.TelegramUpdateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class MyTelegramBot {

    private final TelegramBot telegramBot;

    private final TelegramUpdateService telegramUpdateService;

    private final double currentRate;


    public MyTelegramBot(@Value("${telegram.bot.token}") String botToken, TelegramUpdateService telegramUpdateService) {
        this.telegramBot = new TelegramBot(botToken);
        this.telegramUpdateService = telegramUpdateService;
        this.currentRate = CurrentRateFromApi.getCurrentRate();
    }

    public void sendMessageToChat(String chatId, String message) {
        SendMessage request = new SendMessage(chatId, message).parseMode(ParseMode.HTML);
        SendResponse response = telegramBot.execute(request);
        System.out.println("Message sent? " + response.isOk());
    }

    public void handleUpdates(Update[] updates) {
        for (Update update : updates) {
            onUpdateReceived(update);
        }
    }

    public void onUpdateReceived(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String text = update.message().text();
            Long chatId = update.message().chat().id();
            Integer messageId = update.message().messageId();

            TelegramUpdate telegramUpdate = new TelegramUpdate();
            telegramUpdate.setUpdateId(Long.valueOf(update.updateId()));
            telegramUpdate.setChatId(chatId);
            telegramUpdate.setMessageText(text);
            telegramUpdate.setMessageId(messageId);
            telegramUpdateService.save(telegramUpdate);

            if (text.equals("/start")) {
               sendMessageToChat(String.valueOf(update.message().chat().id()), "Welcome Currency Helper Telegram Bot!");
            } else if (text.matches("\\d+(?:\\.\\d+)?\\$")) {
                String[] parts = text.split("\\$");
                double amount = Double.parseDouble(parts[0]);
                double convertedAmount = new BigDecimal(amount * currentRate).setScale(3, RoundingMode.HALF_UP).doubleValue();
                sendMessageToChat(String.valueOf(update.message().chat().id()), "Converted amount: " + convertedAmount + " KZT");
            } else if (text.matches("\\d+(?:\\.\\d+)?T$")) {
                String[] parts = text.split("T");
                double amount = Double.parseDouble(parts[0]);
                double convertedAmount = new BigDecimal(amount / currentRate).setScale(3, RoundingMode.HALF_UP).doubleValue();
                sendMessageToChat(String.valueOf(update.message().chat().id()), "Converted amount: " + convertedAmount + " USD");
            } else if (text.equals("/rates")) {
                sendMessageToChat(String.valueOf(update.message().chat().id()), "1 KZT = 0.0022 USD\n1 USD = 448.40 KZT");
            } else if (text.equals("/info")) {
                sendMessageToChat(String.valueOf(update.message().chat().id()), "This simple bot converts USD to KZT and vice versa");
            } else if (text.equals("/history")) {
                List<TelegramUpdate> allByChatId = telegramUpdateService.findAllByChatId(chatId);
                StringBuilder sb = new StringBuilder();
                for (TelegramUpdate m : allByChatId) {
                    sb.append(m.getMessageText()).append("\n");
                }

                sendMessageToChat(String.valueOf(update.message().chat().id()), "Your history:\n" + sb);
            } else if (text.equals("/clear")) {
                List<TelegramUpdate> allByChatId = telegramUpdateService.findAllByChatId(chatId);
                for (TelegramUpdate m : allByChatId) {
                    clearChat(String.valueOf(m.getChatId()), m.getMessageId());
                }
                sendMessageToChat(String.valueOf(update.message().chat().id()), "This simple bot converts USD to KZT and vice versa");
            } else {
                sendMessageToChat(String.valueOf(update.message().chat().id()), "I don't understand you :( Example: 100$, 100T, rates, info, history, clear(only user messages)");
            }
        }
    }


    public GetUpdatesResponse execute(GetUpdates timeout) {
        return telegramBot.execute(timeout);
    }

    public void clearChat(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        BaseResponse response = telegramBot.execute(deleteMessage);
        System.out.println("Chat cleared? " + response.isOk());
    }
}

