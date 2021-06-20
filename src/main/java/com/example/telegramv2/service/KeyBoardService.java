package com.example.telegramv2.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class KeyBoardService {

    public ReplyKeyboardMarkup startPosition(){
        ReplyKeyboardMarkup replyKeyboardMarkup = initialize();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton startButton = new KeyboardButton();
        startButton.setText("/start");
        startButton.setRequestLocation(true);
        keyboardRow.add(startButton);
        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup startSearch(){
        ReplyKeyboardMarkup replyKeyboardMarkup = initialize();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton startButton = new KeyboardButton();
        startButton.setText("далее");
        keyboardRow.add(startButton);
        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup CreateButton(){
        ReplyKeyboardMarkup replyKeyboardMarkup = initialize();

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRowFirst = new KeyboardRow();
        KeyboardRow keyboardRowSecond = new KeyboardRow();
        KeyboardRow keyboardRowThird = new KeyboardRow();

        KeyboardButton rollButton = new KeyboardButton();
        KeyboardButton pizzaButton = new KeyboardButton();
        KeyboardButton cafeButton = new KeyboardButton();
        KeyboardButton restButton = new KeyboardButton();
        KeyboardButton pubButton = new KeyboardButton();
        KeyboardButton coffeeButton = new KeyboardButton();

        rollButton.setText("суши");
        pizzaButton.setText("пиццерия");
        cafeButton.setText("кафе");
        restButton.setText("ресторан");
        pubButton.setText("паб");
        coffeeButton.setText("кофейня");

        keyboardRowFirst.add(rollButton);
        keyboardRowFirst.add(pizzaButton);

        keyboardRowSecond.add(cafeButton);
        keyboardRowSecond.add(restButton);

        keyboardRowThird.add(pubButton);
        keyboardRowThird.add(coffeeButton);

        keyboard.add(keyboardRowFirst);
        keyboard.add(keyboardRowSecond);
        keyboard.add(keyboardRowThird);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup initialize(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }

}
