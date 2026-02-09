package com.rasachk.dailyreportbot.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = UserSession.TABLE_NAME)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {

    public static final String TABLE_NAME = "TBL_USER_SESSION";
    public static final String SEQUENCE_NAME = "SEQ_USER_SESSION";

    @Id
    @GeneratedValue(generator = UserSession.SEQUENCE_NAME, strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = UserSession.SEQUENCE_NAME, sequenceName = UserSession.SEQUENCE_NAME, allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "C_CREATION_DATE")
    @CreationTimestamp
    private Timestamp creationDate;

    @Column(name = "C_LAST_MODIFIED_DATE")
    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_TELEGRAM_USER")
    private TelegramUser telegramUser;

    @Column(name = "C_SESSION_STATE")
    @Enumerated(EnumType.STRING)
    private SessionState sessionState;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "C_PARAMETERS")
    private String parameters;
}
