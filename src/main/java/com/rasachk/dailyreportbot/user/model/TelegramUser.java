package com.rasachk.dailyreportbot.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "TBL_USER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
    @SequenceGenerator(name = "SEQ_USER", sequenceName = "SEQ_USER", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "C_CREATION_DATE")
    @CreationTimestamp
    private Timestamp creationDate;

    @Column(name = "C_TELEGRAM_ID")
    private String telegramId;

    @Column(name = "C_USERNAME")
    private String username;

    @Column(name = "C_FIRST_NAME")
    private String firstName;

    @Column(name = "C_LAST_NAME")
    private String lastName;

    @Column(name = "C_CHAT_ID")
    private String chatId;

}
