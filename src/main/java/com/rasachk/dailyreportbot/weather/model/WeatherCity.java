package com.rasachk.dailyreportbot.weather.model;

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
@Table(name = WeatherCity.TABLE_NAME)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherCity {
    public static final String TABLE_NAME = "TBL_WEATHER_CITY";
    public static final String SEQUENCE_NAME = "SEQ_WEATHER_CITY";

    @Id
    @GeneratedValue(generator = WeatherCity.SEQUENCE_NAME, strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = WeatherCity.SEQUENCE_NAME, sequenceName = WeatherCity.SEQUENCE_NAME, allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "C_CREATION_DATE")
    @CreationTimestamp
    private Timestamp creationDate;

    @Column(name = "C_LAST_MODIFIED_DATE")
    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @Column(name = "C_CITY_NAME")
    private String cityName;

    @Column(name = "C_IS_ACTIVE")
    private Boolean isActive;
}
