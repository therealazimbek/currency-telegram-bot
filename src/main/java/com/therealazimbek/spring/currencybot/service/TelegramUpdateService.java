package com.therealazimbek.spring.currencybot.service;

import com.therealazimbek.spring.currencybot.repository.TelegramUpdateRepository;
import com.therealazimbek.spring.currencybot.model.TelegramUpdate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramUpdateService {

    private final TelegramUpdateRepository telegramUpdateRepository;

    public TelegramUpdateService(TelegramUpdateRepository telegramUpdateRepository) {
        this.telegramUpdateRepository = telegramUpdateRepository;
    }

    public void save(TelegramUpdate telegramUpdate) {
        telegramUpdateRepository.save(telegramUpdate);
    }

    public List<TelegramUpdate> findAllByChatId(Long chatId) {
        return telegramUpdateRepository.findAllByChatId(chatId);
    }
}
