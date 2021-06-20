package com.example.telegramv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@SpringBootApplication
public class Telegramv2Application {

    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi botsAPi = new TelegramBotsApi();
        botsAPi.registerBot(new FastFoodTelegramBot());
        SpringApplication.run(Telegramv2Application.class, args);
    }

}
