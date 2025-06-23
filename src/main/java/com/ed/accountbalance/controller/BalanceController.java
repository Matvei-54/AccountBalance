package com.ed.accountbalance.controller;

import com.ed.accountbalance.model.dto.BalanceDtoCreateRequest;
import com.ed.accountbalance.model.dto.BalanceDtoCreateResponse;
import com.ed.accountbalance.service.BalanceService;
import com.ed.accountbalance.service.IdempotencyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/balance")
@RestController
public class BalanceController {

    private final BalanceService balanceService;
    private final IdempotencyService idempotencyService;

    /**
     * Запрос создание баланса
     * @param request dto с именем баланса и ключом идемпотентности
     * @return dto с параметрами созданного баланса
     */
    @Operation(summary = "Создать баланс", description = "В ответе возвращается dto карты")
    @PostMapping
    public BalanceDtoCreateResponse create(@Valid @RequestBody BalanceDtoCreateRequest request) {

        if(idempotencyService.idempotencyKeyCheck(request.idempotencyKey())){
            return idempotencyService.getResultByIdempotencyKey(request.idempotencyKey(), BalanceDtoCreateResponse.class);
        }
        return balanceService.createBalance(request);
    }
}
