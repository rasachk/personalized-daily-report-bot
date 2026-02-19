package com.rasachk.dailyreportbot.currency.service;

import com.rasachk.dailyreportbot.config.Constants;
import com.rasachk.dailyreportbot.currency.client.TetherlandClient;
import com.rasachk.dailyreportbot.currency.model.CryptoDto;
import com.rasachk.dailyreportbot.currency.model.CryptoResponse;
import com.rasachk.dailyreportbot.currency.model.Currency;
import com.rasachk.dailyreportbot.currency.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final TetherlandClient tetherlandClient;

    @Override
    public List<String> getAvailableCurrencyNames() {
        List<Currency> weatherCityList = currencyRepository.findByIsActiveTrueAndIsDeletedFalse();

        return weatherCityList.stream()
                .map(Currency::getName)
                .toList();
    }

    @Override
    public String getCurrencyRateMessage(Map<String, String> parameters) {

        CryptoResponse cryptoResponse = tetherlandClient.getCryptoCurrencies();

        CryptoDto userCurrency = null;

        for (CryptoDto cryptoDto : cryptoResponse.getData()) {
            if (cryptoDto.getName().equalsIgnoreCase(parameters.get(Constants.CURRENCY_KEY))) {
                userCurrency = cryptoDto;
            }
        }

        return buildCurrencyReminderMessage(userCurrency);
    }

    @Override
    public CryptoResponse test() {
        return tetherlandClient.getCryptoCurrencies();
    }


    public String buildCurrencyReminderMessage(CryptoDto cryptoDto) {
        return "💰 Currency Reminder\n\n" +
                "🪙 " +
                cryptoDto.getName() +
                " (" +
                cryptoDto.getSymbol() +
                ")\n" +
                "💵 Price: $" +
                cryptoDto.getPrice() +
                "\n" +
                "🇮🇷 Price (Toman): " +
                formatNumber(cryptoDto.getTomanAmount()) +
                "\n\n" +
                changeLine("24h Change", cryptoDto.getChanges24h()) +
                changeLine("7d Change", cryptoDto.getChanges7d());
    }

    private String changeLine(String label, Double value) {
        String emoji = value >= 0 ? "📈" : "📉";
        return emoji + " " + label + ": "
                + String.format(Locale.ENGLISH, "%+.2f", value)
                + "%\n";
    }

    private String formatNumber(Long number) {
        return NumberFormat.getInstance(Locale.US).format(number);
    }
}
