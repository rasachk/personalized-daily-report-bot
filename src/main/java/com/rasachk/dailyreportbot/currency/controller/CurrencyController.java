package com.rasachk.dailyreportbot.currency.controller;

import com.rasachk.dailyreportbot.currency.model.CryptoResponse;
import com.rasachk.dailyreportbot.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency/")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("test")
    public CryptoResponse tesCurrency() {
        return currencyService.test();
    }
}
