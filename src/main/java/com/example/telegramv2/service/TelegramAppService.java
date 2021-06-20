package com.example.telegramv2.service;

import com.example.telegramv2.entity.Restaurant;
import com.example.telegramv2.entity.User;
import com.example.telegramv2.repository.RestRepo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Setter
@Getter
@NoArgsConstructor
public class TelegramAppService {

    @Value("${telegram.user.loc}")
    private String loc;

    @Autowired
    private RestRepo restRepo;

    public List<Restaurant> nearYour(User user) throws IOException {
        double latitude = user.getLatitude();
        double longitude = user.getLongitude();
        Document doc = Jsoup.connect("https://yandex.by/maps/" + loc + "/search/Где%20поесть/?ll=" + longitude + "%2C" + latitude +
                "&sll=" + longitude + "%2C" + latitude + "&z=15")
                .userAgent("Chrome/90.0.4430.212")
                .referrer("https://www.google.com/")
                .get();
        Elements listName = doc.select("div.search-business-snippet-view");
        List<Restaurant> restaurants = new ArrayList<>();
        for (Element element : listName) {
            restaurants.add(new Restaurant(
                    element.select("div.search-business-snippet-view__address").text(),
                    element.select("div.search-business-snippet-view__head")
                            .select("div.search-business-snippet-view__title").text(),
                    element.select("span.business-rating-badge-view__rating-text._size_m").text(),
                    element.select("div.search-business-snippet-subtitle-view._theme_black")
                            .select("div.search-business-snippet-subtitle-view__title").text(),
                    element.select("div.business-working-status-view").text(),
                    element.select("a.search-business-snippet-view__category").text(),
                    element.select("img").attr("src"),
                    user
            ));
        }
        restRepo.saveAll(restaurants);
        return restaurants;
    }

    public Restaurant findNearRestaurant(List<Restaurant> restaurants, User user) throws IOException {
        double latitude = user.getLatitude();
        double longitude = user.getLongitude();
        for (Restaurant restaurant : restaurants) {
            Document document = Jsoup.connect("https://yandex.by/maps/?ll=" + latitude +
                    "%2C" + longitude + "&mode=routes&rtext=" + latitude + "%2C" + longitude + "~"
                    + restaurant.getAddress() + "&z=17")
                    .userAgent("Chrome/90.0.4430.212")
                    .referrer("https://www.google.com/")
                    .get();
            Elements route = document.select("div.auto-route-snippet-view__route-subtitle");
            List<String> routes = Arrays.asList(route.text().split(" "));
            if(routes.get(1).contains("км")){
                double rou = Double.parseDouble(routes.get(0).replace(",", "."));
                restaurant.setRoute((int) (rou*1000));
            } else {
                restaurant.setRoute(Integer.parseInt(routes.get(0)));
            }
        }
        Restaurant rest = restaurants.get(0);
        for (Restaurant restaurant : restaurants) {
            if(rest.getRoute() > restaurant.getRoute()){
                rest = restaurant;
            }
        }
        return rest;
    }

    public User clearUser(User user){
        user.setSteps(1);
        user.setLongitude(0);
        user.setLatitude(0);
        user.setRestaurantList(null);
        return user;
    }

    public SendMessage filterNearRes(User user, String filter) throws IOException {
        SendMessage send = new SendMessage();
        double latitude = user.getLatitude();
        double longitude = user.getLongitude();
        List<Restaurant> filterRests = user.getRestaurantList().stream()
                .filter(x -> x.getCategories().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());
        Restaurant rest = findNearRestaurant(filterRests, user);
        List<String> adress = Arrays.asList(rest.getAddress().split(" "));
        StringBuilder preAdress = new StringBuilder("");
        for (String s : adress) {
            preAdress.append(s + "+");
        }
        send.setChatId(user.getTelegramChatId());
        String url = "https://yandex.by/maps/?ll=" + longitude  + "%2C" + latitude +
                "&mode=routes&rtext=" + latitude + "%2C" + longitude + "~" + preAdress + "&rtt=auto&ruri=~&z=17";
        send.setText(
                "Название: " +  rest.getName() + "\n" +
                        "Cредний чек: " + rest.getMediumPrice() + "\n" +
                        "Оценки: " + rest.getStars() + " Звёзд" + "\n" +
                        "Время работы: " + rest.getWorkTime() + "\n" +
                        "Расстояние до места: " + rest.getRoute() + " м" + "\n" +
                        rest.getUrlImg() + "\n"  +
                        "путь находиться по ссылке: " + url
        );
        return send;
    }

    public boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }
}
