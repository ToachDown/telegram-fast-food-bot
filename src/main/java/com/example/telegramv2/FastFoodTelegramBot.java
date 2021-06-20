package com.example.telegramv2;

import com.example.telegramv2.entity.User;
import com.example.telegramv2.repository.UserRepo;
import com.example.telegramv2.service.KeyBoardService;
import com.example.telegramv2.service.TelegramAppService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Locale;

@Service
@Getter
@Setter
public class FastFoodTelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TelegramAppService telegramAppService;

    @Autowired
    private KeyBoardService keyBoardService;

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.key}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SendMessage send = new SendMessage();
        User user = userRepo.findByTelegramChatId(message.getChatId());
        if(user == null){
            user = new User();
            user.setTelegramChatId(message.getChatId());
            user.setSteps(1);
        }
        send.setChatId(user.getTelegramChatId());
        try {
            switch (user.getSteps()) {
                case 1: {
                    send.setReplyMarkup(keyBoardService.startPosition())
                        .setText("начнём?");
                    execute(send);
                    user.setSteps(2);
                    userRepo.save(user);
                }
                case 2: {
                    if(message.getLocation() != null) {
                        Location location = message.getLocation();
                        user.setLatitude(location.getLatitude());
                        user.setLongitude(location.getLongitude());
                        user.setRestaurantList(telegramAppService.nearYour(user));
                        send.setReplyMarkup(keyBoardService.startSearch())
                                .setText("Поиск мест...");
                        execute(send);
                        user.setSteps(3);
                        userRepo.save(user);
                    } else {
                        send.setText("вы ввели некорректный адресс");
                        execute(send);
                    }
                }
                case 3:{
                    if(message.getText().toLowerCase(Locale.ROOT).equals("далее")){
                        send.setText("введите сумму на которую вы хотите поесть (BYN): ");
                        execute(send);
                        user.setSteps(4);
                        userRepo.save(user);
                    }
                }
                case 4: {
                    if(telegramAppService.isNumber(message.getText())) {
                        user.setSum(Integer.parseInt(message.getText()));
                        ReplyKeyboardMarkup replyKeyboardMarkup = keyBoardService.CreateButton();
                        send.setText("вы хотите покушать на " + user.getSum() + " BYN")
                            .setReplyMarkup(replyKeyboardMarkup);
                        execute(send);
                        user.setSteps(5);
                        userRepo.save(user);
                    } else {
                        send.setChatId(user.getTelegramChatId()).setText("вы ввели некорректную сумму");
                        execute(send);
                    }
                }
                case 5: {
                    if(message.hasText()) {
                        switch (message.getText().toLowerCase(Locale.ROOT)) {
                            case "суши":
                            case "пиццерия":
                            case "ресторан":
                            case "паб":
                            case "кафе":
                            case "кофейня": {
                                send = telegramAppService.filterNearRes(user, message.getText().toLowerCase(Locale.ROOT));
                                execute(send);
                                userRepo.save(telegramAppService.clearUser(user));
                                break;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
