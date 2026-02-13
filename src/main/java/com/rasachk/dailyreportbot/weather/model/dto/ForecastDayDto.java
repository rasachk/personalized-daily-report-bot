package com.rasachk.dailyreportbot.weather.model.dto;

import lombok.Data;

@Data
public class ForecastDayDto {
    private String date;
    private DayDto day;
    private AstroDto astro;
}
