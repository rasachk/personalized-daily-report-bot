package com.rasachk.dailyreportbot.weather.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DayDto {
    @JsonProperty("maxtemp_c")
    private Double maxTempC;
    @JsonProperty("mintemp_c")
    private Double minTempC;
    @JsonProperty("avgtemp_c")
    private Double avgTempC;
    @JsonProperty("maxwind_kph")
    private Double maxWindKph;
    @JsonProperty("totalprecip_mm")
    private Double totalPrecipMm;
    @JsonProperty("avgvis_km")
    private Double avgVisKm;
    @JsonProperty("avghumidity")
    private Double avgHumidity;
    @JsonProperty("uv")
    private Double uv;
    @JsonProperty("daily_chance_of_rain")
    private Integer dailyChanceOfRain;
    @JsonProperty("condition")
    private ConditionDto condition;
}
