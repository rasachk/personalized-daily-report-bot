package com.rasachk.dailyreportbot.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "TBL_USER")
public class User {
    @Id
    private Long id;
    private String telegramId;
    private String telegramUsername;
    private String name;
}
