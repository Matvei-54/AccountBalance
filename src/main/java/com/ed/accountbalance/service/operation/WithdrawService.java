package com.ed.accountbalance.service.operation;

import com.ed.accountbalance.exception.transaction.InsufficientFundsException;
import com.ed.accountbalance.model.entity.Balance;
import com.ed.accountbalance.model.entity.Transaction;
import com.ed.accountbalance.model.enums.Currency;
import com.ed.accountbalance.model.enums.TransactionStatus;
import com.ed.accountbalance.model.enums.TransactionType;
import com.ed.accountbalance.repository.BalanceRepository;
import com.ed.accountbalance.repository.TransactionRepository;
import com.ed.accountbalance.service.converter.CurrencyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class WithdrawService implements TransactionCommandHandler {

    private final TransactionRepository transactionRepository;
    private final CurrencyConverter currencyConverter;
    private final BalanceRepository balanceRepository;

    /**
     * Метод вывода средств с баланса.
     * @param amount сумма вывода без конвертации.
     * @param balance значение баланса в USD
     * @param currency тип валюты
     * @return сущность транзакции, предварительно сохраненную в postgres
     */
    @Transactional
    public Transaction process(BigDecimal amount, Balance balance, Currency currency) {

        BigDecimal withdrawAmount = currencyConverter.convert(currency.toString(), amount);

        if(balance.getBalance().compareTo(withdrawAmount) < 0){
            throw new InsufficientFundsException(amount);
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setOwnerBalance(balance);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);

        balance.setBalance(balance.getBalance().subtract(withdrawAmount));
        balanceRepository.save(balance);
        return transaction;
    }
}
