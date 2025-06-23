package com.ed.accountbalance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplicationTaskScheduler {

    private final RateManageService rateRequestService;

    @Scheduled(cron = "0 0 1 * * ?", zone = "Europe/Moscow")
    public void executeTaskUpdateRateCurrency() {
        rateRequestService.updateCurrencyExchangeRate();
    }
}
