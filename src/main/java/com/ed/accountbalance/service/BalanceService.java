package com.ed.accountbalance.service;

import com.ed.accountbalance.exception.balance.BalanceWithNameAlreadyExistsException;
import com.ed.accountbalance.mapper.BalanceMapper;
import com.ed.accountbalance.model.dto.BalanceDtoCreateRequest;
import com.ed.accountbalance.model.dto.BalanceDtoCreateResponse;
import com.ed.accountbalance.model.entity.Balance;
import com.ed.accountbalance.model.enums.AccountStatus;
import com.ed.accountbalance.model.enums.Currency;
import com.ed.accountbalance.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BalanceService {

    private static final long TIME_LIFE_RECORD_DB = 3600;

    private final BalanceRepository balanceRepository;

    private final BalanceMapper balanceMapper;
    private final IdempotencyService idempotencyService;

    /**
     * Метод создание balance
     * @return dto созданного баланса
     */
    @Transactional
    public BalanceDtoCreateResponse createBalance(BalanceDtoCreateRequest request) {
        if(balanceRepository.findByAccountName(request.accountName()).isPresent()){
            log.error("Balance with name {} already exists", request.accountName());
            throw new BalanceWithNameAlreadyExistsException(request.accountName());
        }

        Balance balance = new Balance();
        balance.setAccountName(request.accountName());
        balance.setCurrency(Currency.USD);
        balance.setAccountStatus(AccountStatus.ACTIVE);
        BalanceDtoCreateResponse response = balanceMapper.balanceToDto(balanceRepository.save(balance));

        idempotencyService.saveIdempotencyKey(request.idempotencyKey(), response, TIME_LIFE_RECORD_DB);

        return response;
    }
}
