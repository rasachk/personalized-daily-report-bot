package com.rasachk.dailyreportbot.currency.client;

import com.rasachk.dailyreportbot.currency.model.CryptoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class TetherlandClient {

    private final RestTemplate restTemplate;

    public CryptoResponse getCryptoCurrencies() {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://service.tetherland.com/api/v5/currencies")
                .toUriString();

        return restTemplate.getForObject(url, CryptoResponse.class);
    }
}
