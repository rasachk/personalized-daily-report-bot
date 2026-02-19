package com.rasachk.dailyreportbot.currency.model;

import lombok.Data;

import java.util.List;

@Data
public class CryptoResponse {
    private List<CryptoDto> data;
}
