package com.rasachk.dailyreportbot.weather.service;

import com.rasachk.dailyreportbot.weather.model.dto.ForecastResponse;

import java.util.List;
import java.util.Map;

public interface WeatherForcastService {

    List<String> getAvailableCityNames();

    String getWeatherForcastMessage(Map<String, String> parameters);

    ForecastResponse getWeatherForecast(String city, Integer days);
}
