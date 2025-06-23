package com.ed.accountbalance.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CurrencyConverter {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;


    public BigDecimal convert(String toCurrency, BigDecimal amount) {

        if (toCurrency.isBlank()) {
            return amount;
        }
        return amount.multiply(mapper.convertValue(redisTemplate.opsForValue().get(toCurrency), BigDecimal.class));
    }
}
