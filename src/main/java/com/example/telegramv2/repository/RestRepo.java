package com.example.telegramv2.repository;

import com.example.telegramv2.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestRepo extends JpaRepository<Restaurant, Long> {
}
