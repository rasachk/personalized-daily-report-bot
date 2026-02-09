package com.rasachk.dailyreportbot.reminder.model;

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
import java.time.LocalTime;

@Entity
@Data
@Table(name = Reminder.TABLE_NAME)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reminder {

    public static final String TABLE_NAME = "TBL_REMINDER";
    public static final String SEQUENCE_NAME = "SEQ_REMINDER";

    @Id
    @GeneratedValue(generator = Reminder.SEQUENCE_NAME, strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = Reminder.SEQUENCE_NAME, sequenceName = Reminder.SEQUENCE_NAME, allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "C_CREATION_DATE")
    @CreationTimestamp
    private Timestamp creationDate;

    @Column(name = "C_LAST_MODIFIED_DATE")
    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @Column(name = "C_REMINDER_TYPE")
    @Enumerated(EnumType.STRING)
    private ReminderType reminderType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "C_PARAMETERS")
    private String parameters;

    @Column(name = "C_REMINDER_TIME")
    private LocalTime reminderTime;

    @Column(name = "C_IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "C_IS_DELETED")
    private Boolean isDeleted;

}
