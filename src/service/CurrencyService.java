package service;

import api.ExchangeRateAPI;
import model.Currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyService {

    ExchangeRateAPI searchAPI;
    Map<String, String> conversionRates;
    List<Currency> currencyList;

    public CurrencyService() {
        this.searchAPI = new ExchangeRateAPI();
        this.conversionRates = new HashMap<>();
        this.currencyList = new ArrayList<>();
    }

    public void supportedCodes() {
        try {
            Map<String, String> supportedCodes = searchAPI.supportedCodes();

            if (!supportedCodes.isEmpty()) {
                setCodeAndName(supportedCodes);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los códigos: " + e.getMessage());
        }
    }

    public void setCodeAndName(Map<String, String> supportedCodes) {
        for (Map.Entry<String, String> entry : supportedCodes.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue();
            if (currencyList.stream().noneMatch(currency -> currency.getCode().equals(code))) {
                Currency currency = new Currency();
                currency.setCode(code);
                currency.setName(name);
                currencyList.add(currency);
            }
        }
        showCurrencyList(currencyList);
    }

    public void showCurrencyList(List<Currency> currencyList) {
        StringBuilder message = new StringBuilder("""
                -------------------------------------------------------
                Las monedas disponibles son:
                -------------------------------------------------------
                """);

        for (Currency currency : currencyList) {
            message.append(" --> Código: ").append(currency.getCode()).append(" País: ").append(currency.getName()).append("\n");
        }
        System.out.println(message.toString());
    }

    public void currencyRates(String codeBase) {

        if (verifyCodeConversionRates(codeBase)) {
            conversionRates = searchAPI.conversionRate(codeBase);
            setConversionRates(codeBase);
        } else {
            System.out.println("Código no válido");
        }
    }

    public boolean verifyCodeConversionRates(String codeBase) {
        return currencyList.stream().anyMatch(currency -> currency.getCode().equalsIgnoreCase(codeBase));
    }

    public void setConversionRates(String codeBase) {
        currencyList.stream()
                .filter(currency -> currency.getCode().equalsIgnoreCase(codeBase) && currency.getConversionRates() == null)
                .findFirst()
                .ifPresent(currency -> {
                    currency.setConversionRates(conversionRates);
                    System.out.println("Las tasas de conversión para: " + currency.getCode() + " --> " + currency.getName());
                    showConversionRates();
                });
    }

    public void showConversionRates() {
        for (Map.Entry<String, String> entry : conversionRates.entrySet()) {
            System.out.println("Código: " + entry.getKey() + " --> " + entry.getValue());
        }
    }
}
