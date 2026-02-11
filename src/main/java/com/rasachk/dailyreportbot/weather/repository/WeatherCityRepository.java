package com.rasachk.dailyreportbot.weather.repository;

import com.rasachk.dailyreportbot.weather.model.WeatherCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherCityRepository extends JpaRepository<WeatherCity, Long> {
    List<WeatherCity> findAllByIsActive(Boolean isActive);
}
