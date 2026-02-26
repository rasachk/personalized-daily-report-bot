package com.rasachk.dailyreportbot.weather.service;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.weather.client.WeatherAPIClient;
import com.rasachk.dailyreportbot.weather.model.WeatherCity;
import com.rasachk.dailyreportbot.weather.model.dto.AstroDto;
import com.rasachk.dailyreportbot.weather.model.dto.DayDto;
import com.rasachk.dailyreportbot.weather.model.dto.ForecastDayDto;
import com.rasachk.dailyreportbot.weather.model.dto.ForecastResponse;
import com.rasachk.dailyreportbot.weather.repository.WeatherCityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Log4j2
public class WeatherForecastServiceImpl implements WeatherForecastService {

    private final WeatherCityRepository weatherCityRepository;
    private final WeatherAPIClient weatherAPIClient;

    @Override
    public List<String> getAvailableCityNames() {
        List<WeatherCity> weatherCityList = weatherCityRepository.findAllByIsActive(true);

        return weatherCityList.stream()
                .map(WeatherCity::getCityName)
                .toList();
    }

    @Override
    public String getWeatherForcastMessage(Map<String, String> parameters) {
        String location = parameters.get(Constants.LOCATION_KEY);
        log.info("Getting weather forecast for location: {}", location);
        ForecastResponse forecastResponse = weatherAPIClient.getForecast(location, 3);
        return buildForecastMessage(forecastResponse, location);
    }

    @Override
    public ForecastResponse getWeatherForecast(String city, Integer days) {
        return weatherAPIClient.getForecast(city, days);
    }


    private String buildForecastMessage(ForecastResponse forecastResponse, String location) {
        DateTimeFormatter INPUT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter OUTPUT = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        StringBuilder msg = new StringBuilder();

        msg.append("🌤 Weather forecast for ").append(location).append("  (3 days)\n\n");

        for (ForecastDayDto dayData : forecastResponse.getForecast().getForecastDay()) {

            LocalDate date = LocalDate.parse(dayData.getDate(), INPUT);
            DayDto day = dayData.getDay();
            AstroDto astro = dayData.getAstro();

            msg.append("📅 ").append(date.format(OUTPUT)).append("\n");
            msg.append("Condition: ")
                    .append(day.getCondition().getText()).append("\n");

            msg.append("🌡 Temp: ")
                    .append(round(day.getMinTempC())).append("°C — ")
                    .append(round(day.getMaxTempC())).append("°C ")
                    .append("(avg ").append(round(day.getAvgTempC()))
                    .append("°C)\n");

            msg.append("🌧 Rain chance: ")
                    .append(day.getDailyChanceOfRain()).append("% (")
                    .append(round(day.getTotalPrecipMm())).append(" mm)\n");

            msg.append("💧 Humidity: ")
                    .append(round(day.getAvgHumidity())).append("%\n");

            msg.append("💨 Wind: up to ")
                    .append(round(day.getMaxWindKph())).append(" kph\n");

            msg.append("🔆 UV index: ")
                    .append(round(day.getUv())).append("\n");

            msg.append("🌅 Sunrise: ")
                    .append(astro.getSunrise())
                    .append(" | 🌇 Sunset: ")
                    .append(astro.getSunset())
                    .append("\n\n");
        }

        return msg.toString();
    }

    private String round(double value) {
        return String.format(Locale.ENGLISH, "%.1f", value);
    }
}
