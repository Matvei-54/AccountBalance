package com.ed.accountbalance.service.operation;

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
public class DepositService implements TransactionCommandHandler {

    private final TransactionRepository transactionRepository;
    private final CurrencyConverter currencyConverter;
    private final BalanceRepository balanceRepository;

    @Transactional
    public Transaction process(BigDecimal amount, Balance balance, Currency currency) {

        Transaction transaction = new Transaction();
        transaction.setCurrency(currency);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setOwnerBalance(balance);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);

        balance.setBalance(balance.getBalance().add(currencyConverter.convert(currency.toString(), amount)));
        balanceRepository.save(balance);

        return transaction;
    }
}
