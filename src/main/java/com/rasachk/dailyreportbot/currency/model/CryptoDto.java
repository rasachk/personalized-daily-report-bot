package com.rasachk.dailyreportbot.currency.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CryptoDto {

    private String name;
    private String symbol;
    private String price;

    @JsonProperty("toman_amount")
    private Long tomanAmount;

    @JsonProperty("changes_24h")
    private Double changes24h;

    @JsonProperty("changes_7d")
    private Double changes7d;
}
