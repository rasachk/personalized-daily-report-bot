package com.rasachk.dailyreportbot.currency.service;

import com.rasachk.dailyreportbot.currency.model.CryptoResponse;

import java.util.List;
import java.util.Map;

public interface CurrencyService {

    List<String> getAvailableCurrencyNames();

    String getCurrencyRateMessage(Map<String,String> parameters);

    CryptoResponse test();

}
