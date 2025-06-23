package com.ed.accountbalance.config;

import com.ed.accountbalance.model.enums.TransactionType;
import com.ed.accountbalance.service.operation.DepositService;
import com.ed.accountbalance.service.operation.TransactionCommandHandler;
import com.ed.accountbalance.service.operation.WithdrawService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TransactionsCommandConfig {

    @Bean
    public Map<TransactionType, TransactionCommandHandler> transactionCommandHandlers(
            DepositService depositService,
            WithdrawService withdrawService
    ){
        Map<TransactionType, TransactionCommandHandler> commandHandler = new HashMap<>();
        commandHandler.put(TransactionType.DEPOSIT, depositService);
        commandHandler.put(TransactionType.WITHDRAWAL, withdrawService);
        return commandHandler;
    }

}
