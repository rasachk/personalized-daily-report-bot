package com.rasachk.dailyreportbot.weather.controller;

import com.rasachk.dailyreportbot.weather.model.dto.ForecastResponse;
import com.rasachk.dailyreportbot.weather.service.WeatherForcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weatherForcast/")
@RequiredArgsConstructor
public class WeatherForecastController {

    private final WeatherForcastService weatherForcastService;

    @GetMapping("test")
    public ForecastResponse testWeatherForecast() {
        return weatherForcastService.getWeatherForecast("Tehran", 3);
    }
}
