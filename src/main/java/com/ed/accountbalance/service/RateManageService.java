package com.ed.accountbalance.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RateManageService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RestTemplate restTemplate;
    private static final List<String> CURRENCY_CODES = List.of("USD", "EUR","BYN","RUB", "GBP");
    private final Map<String, BigDecimal> currencyRateAtStartApp;
    private static final long cacheHours = 24;

    @PostConstruct
    public void init(){
        currencyRateAtStartApp.forEach((key, value) -> redisTemplate.opsForValue().set(key, value, cacheHours, TimeUnit.HOURS));
    }


    @Transactional
    public void updateCurrencyExchangeRate(){
        //TODO
    }
}
