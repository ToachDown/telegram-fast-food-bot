package com.example.telegramv2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String address;
    private String urlImg;
    private String name;
    private String stars;
    private String mediumPrice;
    private String workTime;
    private String categories;
    private int route;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Restaurant(String address, String urlImg, String name, String stars, String mediumPrice, String workTime, String categories, User user) {
        this.address = address;
        this.urlImg = urlImg;
        this.name = name;
        this.stars = stars;
        this.mediumPrice = mediumPrice;
        this.workTime = workTime;
        this.categories = categories;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", user=" + user + '\'' +
                ", address='" + address + '\'' +
                ", urlImg='" + urlImg + '\'' +
                ", name='" + name + '\'' +
                ", stars='" + stars + '\'' +
                ", mediumPrice='" + mediumPrice + '\'' +
                ", workTime='" + workTime + '\'' +
                ", categories='" + categories + '\'' +
                ", route=" + route +
                '}';
    }
}
