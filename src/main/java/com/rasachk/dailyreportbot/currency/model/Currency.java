package com.rasachk.dailyreportbot.currency.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = Currency.TABLE_NAME)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    public static final String TABLE_NAME = "TBL_CURRENCY";
    public static final String SEQUENCE_NAME = "SEQ_CURRENCY";

    @Id
    @GeneratedValue(generator = Currency.SEQUENCE_NAME, strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = Currency.SEQUENCE_NAME, sequenceName = Currency.SEQUENCE_NAME, allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "C_CREATION_DATE")
    @CreationTimestamp
    private Timestamp creationDate;

    @Column(name = "C_LAST_MODIFIED_DATE")
    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @Column(name = "C_NAME")
    private String name;

    @Column(name = "C_IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "C_IS_DELETED")
    private Boolean isDeleted;

}
