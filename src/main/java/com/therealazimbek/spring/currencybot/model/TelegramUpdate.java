package com.therealazimbek.spring.currencybot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "telegram_update")
public class TelegramUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long updateId;
    private Long chatId;
    private String messageText;
    private Integer messageId;
}