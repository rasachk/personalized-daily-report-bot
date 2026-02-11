package com.rasachk.dailyreportbot.weather.service;

import com.rasachk.dailyreportbot.weather.model.WeatherCity;
import com.rasachk.dailyreportbot.weather.repository.WeatherCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WeatherForcastServiceImpl implements WeatherForcastService {

    private final WeatherCityRepository weatherCityRepository;

    @Override
    public List<String> getAvailableCityNames() {
        List<WeatherCity> weatherCityList = weatherCityRepository.findAllByIsActive(true);

        return weatherCityList.stream()
                .map(WeatherCity::getCityName)
                .toList();
    }
}
