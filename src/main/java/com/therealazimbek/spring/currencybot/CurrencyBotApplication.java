package com.therealazimbek.spring.currencybot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.therealazimbek.spring.currencybot.config.MyTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrencyBotApplication implements CommandLineRunner {

    private final MyTelegramBot telegramBot;

    @Autowired
    public CurrencyBotApplication(MyTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public static void main(String[] args) {
        SpringApplication.run(CurrencyBotApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Replace with your chat ID
        String chatId = "735365473";
        String message = "Hello from your Telegram bot!";
        while (true) {
            GetUpdatesResponse updatesResponse = telegramBot.execute(new GetUpdates().limit(100).offset(0).timeout(0));
            Update[] updates = updatesResponse.updates().toArray(new Update[0]);

            // Pass the updates to the bot for handling
            telegramBot.handleUpdates(updates);

            // Increase the offset to avoid processing the same updates again
            if (updates.length > 0) {
                int lastUpdateId = updates[updates.length - 1].updateId();
                telegramBot.execute(new GetUpdates().offset(lastUpdateId + 1));
            }
        }
    }
}
