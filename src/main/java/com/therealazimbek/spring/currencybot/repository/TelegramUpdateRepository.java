package com.therealazimbek.spring.currencybot.repository;

import com.therealazimbek.spring.currencybot.model.TelegramUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TelegramUpdateRepository extends JpaRepository<TelegramUpdate, Long> {

    List<TelegramUpdate> findAllByChatId(Long chatId);
}
