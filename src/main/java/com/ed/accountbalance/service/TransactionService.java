package com.ed.accountbalance.service;

import com.ed.accountbalance.exception.balance.BalanceWithNameNoExistsException;
import com.ed.accountbalance.exception.transaction.UnrecognizedOperationTypeException;
import com.ed.accountbalance.mapper.TransactionMapper;
import com.ed.accountbalance.model.dto.TransactionDtoCreateRequest;
import com.ed.accountbalance.model.dto.TransactionDtoCreateResponse;
import com.ed.accountbalance.model.dto.TransactionDtoResponse;
import com.ed.accountbalance.model.entity.Balance;
import com.ed.accountbalance.model.entity.Transaction;
import com.ed.accountbalance.model.enums.Currency;
import com.ed.accountbalance.model.enums.TransactionType;
import com.ed.accountbalance.repository.BalanceRepository;
import com.ed.accountbalance.repository.TransactionRepository;
import com.ed.accountbalance.service.operation.TransactionCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final IdempotencyService idempotencyService;
    private static final long TIME_LIFE_RECORD_DB = 3600;
    private final TransactionMapper transactionMapper;
    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;
    private final Map<TransactionType, TransactionCommandHandler> transactionsCommandHandlers;

    /**
     * Метод выполнение транзакции, определяется тип операции и на его основе вызывается исполняющий метод из Map.
     * Map по ключу выбирает класс, который выполнит операцию, зависящую от типа, паттерн Стратегия
     * @param transactionDto с параметрами операции
     * @return
     */
    @Transactional
    public TransactionDtoCreateResponse createTransaction(TransactionDtoCreateRequest transactionDto,
                                                          TransactionType transactionType,
                                                          Currency currency) {

        Balance balance = balanceRepository.findByAccountNameWithLock(transactionDto.accountName())
                .orElseThrow(() -> new BalanceWithNameNoExistsException(transactionDto.accountName()));

        if(transactionType != TransactionType.DEPOSIT && transactionType != TransactionType.WITHDRAWAL){
            throw new UnrecognizedOperationTypeException(transactionType.toString());
        }

        Transaction transaction = transactionsCommandHandlers.get(transactionType)
                .process(transactionDto.amount(), balance, currency);

        TransactionDtoCreateResponse response = transactionMapper
                .transactionToDtoCreate(transaction);

        idempotencyService.saveIdempotencyKey(transactionDto.idempotencyKey(), response, TIME_LIFE_RECORD_DB);

        return response;
    }

    @Transactional
    public Page<TransactionDtoResponse> getTransactions(String balanceName, int page, int size) {
        Balance balance = balanceRepository.findByAccountName(balanceName)
                .orElseThrow(() -> new BalanceWithNameNoExistsException(balanceName));

        Pageable pageable = PageRequest.of(page,size);

        return transactionRepository.findByOwnerBalanceId(balance.getId(), pageable)
                .map(transactionMapper::transactionToDto);
    }
}
