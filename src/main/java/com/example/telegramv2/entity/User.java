package com.example.telegramv2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long telegramChatId;
    private double latitude;
    private double longitude;
    private int steps;
    private int sum;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Restaurant> restaurantList;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", telegramChatId=" + telegramChatId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", steps=" + steps +
                ", sum=" + sum +
                '}';
    }
}
