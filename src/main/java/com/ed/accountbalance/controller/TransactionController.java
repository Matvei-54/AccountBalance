package com.ed.accountbalance.controller;


import com.ed.accountbalance.model.dto.TransactionDtoCreateRequest;
import com.ed.accountbalance.model.dto.TransactionDtoCreateResponse;
import com.ed.accountbalance.model.dto.TransactionDtoResponse;
import com.ed.accountbalance.model.enums.Currency;
import com.ed.accountbalance.model.enums.TransactionType;
import com.ed.accountbalance.service.IdempotencyService;
import com.ed.accountbalance.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/transaction")
@RestController
public class TransactionController {

    private final TransactionService transactionService;
    private final IdempotencyService idempotencyService;

    /**
     * Запрос создание транзакции - депозит или вывод средств
     * @param request request dto с параметрами операции и ключом идемпотентности
     * @param transactionType тип операции, выбранный пользователем в форме
     * @param currency имя валюты, выбранной пользователем в форме
     * @return dto с параметрами созданного транзакции
     */
    @Operation(summary = "Исполнение финансовой операции", description = "В ответе возвращается dto карты")
    @PostMapping("/operation")
    public TransactionDtoCreateResponse operation(@Valid @RequestBody TransactionDtoCreateRequest request,
                                                  @RequestParam("transactionType") TransactionType transactionType,
                                                  @RequestParam("Currency") Currency currency) {

        if(idempotencyService.idempotencyKeyCheck(request.idempotencyKey())){
            return idempotencyService.getResultByIdempotencyKey(request.idempotencyKey(), TransactionDtoCreateResponse.class);
        }
        return transactionService.createTransaction(request, transactionType, currency);
    }

    /**
     * Получить список всех транзакций
     * @param accountName имя баланса для поиска траназакций
     * @param page
     * @param size
     * @return Лист транзакций
     */
    @GetMapping()
    public Page<TransactionDtoResponse> getAllTransactionsByAccountName(
            @RequestParam("accountName") String accountName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transactionService.getTransactions(accountName, page, size);
        }
}
