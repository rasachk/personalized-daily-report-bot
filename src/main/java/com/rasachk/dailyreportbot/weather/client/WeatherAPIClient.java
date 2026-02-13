package com.rasachk.dailyreportbot.weather.client;

import com.rasachk.dailyreportbot.weather.model.dto.ForecastResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class WeatherAPIClient {

    private final RestTemplate restTemplate;

    @Value("${api.key.weather}")
    private String weatherApiKey;

    public ForecastResponse getForecast(String city, Integer days) {

        String url = UriComponentsBuilder
                .fromHttpUrl("http://api.weatherapi.com/v1/forecast.json")
                .queryParam("key", weatherApiKey)
                .queryParam("q", city)
                .queryParam("days", days)
                .toUriString();

        return restTemplate.getForObject(url, ForecastResponse.class);
    }

}
