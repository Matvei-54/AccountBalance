package com.ed.accountbalance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CurrencyRateConfig {

    @Bean
    public Map<String, BigDecimal> currencyRateAtStartApp(){
        Map<String, BigDecimal> currencyRateInit = new HashMap<>();
        currencyRateInit.put("USD", new BigDecimal("1.0"));
        currencyRateInit.put("EUR", new BigDecimal("1.1"));
        currencyRateInit.put("BYN", new BigDecimal("0.31"));
        currencyRateInit.put("RUB", new BigDecimal("0.013"));
        currencyRateInit.put("GBP", new BigDecimal("1.34"));

        return currencyRateInit;
    }
}
