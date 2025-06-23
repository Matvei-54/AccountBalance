package com.ed.accountbalance.service.operation;

import com.ed.accountbalance.model.entity.Balance;
import com.ed.accountbalance.model.entity.Transaction;
import com.ed.accountbalance.model.enums.Currency;

import java.math.BigDecimal;

public interface TransactionCommandHandler {

    Transaction process(BigDecimal amount, Balance balance, Currency currency);
}
