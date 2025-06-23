package com.ed.accountbalance.balance;

import com.ed.accountbalance.exception.balance.BalanceWithNameAlreadyExistsException;
import com.ed.accountbalance.mapper.BalanceMapper;
import com.ed.accountbalance.model.dto.BalanceDtoCreateRequest;
import com.ed.accountbalance.model.dto.BalanceDtoCreateResponse;
import com.ed.accountbalance.model.entity.Balance;
import com.ed.accountbalance.model.enums.AccountStatus;
import com.ed.accountbalance.model.enums.Currency;
import com.ed.accountbalance.repository.BalanceRepository;
import com.ed.accountbalance.service.BalanceService;
import com.ed.accountbalance.service.IdempotencyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceMapper balanceMapper;

    @Mock
    private IdempotencyService idempotencyService;

    @InjectMocks
    private BalanceService balanceService;



    @DisplayName("Создание баланса, со статусом успешно")
    @Test
    void createBalance_WhenAccountNameIsUnique_ReturnBalanceDto() {
        // Given
        String accountName = "test-account";
        String idempotencyKey = "123e4567-e89b-12d3-a456-426614174000";
        BalanceDtoCreateRequest request = new BalanceDtoCreateRequest(accountName, idempotencyKey);

        Balance balance = new Balance();
        balance.setAccountName(accountName);
        balance.setCurrency(Currency.USD);
        balance.setAccountStatus(AccountStatus.ACTIVE);

        BalanceDtoCreateResponse expectedResponse =
                new BalanceDtoCreateResponse("test-account", accountName, BigDecimal.ZERO, AccountStatus.ACTIVE.toString());

        Mockito.when(balanceRepository.findByAccountName(accountName)).thenReturn(Optional.empty());
        Mockito.when(balanceRepository.save(any(Balance.class))).thenReturn(balance);
        Mockito.when(balanceMapper.balanceToDto(balance)).thenReturn(expectedResponse);

        // When
        BalanceDtoCreateResponse actualResponse = balanceService.createBalance(request);

        // Then
        assertThat(actualResponse).isEqualTo(expectedResponse);

        // Проверяем, что методы вызвались ровно по одному разу
        Mockito.verify(balanceRepository).findByAccountName(accountName);
        Mockito.verify(balanceRepository).save(any(Balance.class));
        Mockito.verify(balanceMapper).balanceToDto(balance);
        Mockito.verify(idempotencyService).saveIdempotencyKey(idempotencyKey, expectedResponse, 3600);
    }


    @DisplayName("Создание баланса, со ожидаем исключение, если имя баласна повторяется")
    @Test
    void createBalance_WhenAccountNameExists_ThrowException() {

        String existingAccountName = "duplicate-account";
        String idempotencyKey = "123e4567-e89b-12d3-a456-426614174000";
        BalanceDtoCreateRequest request = new BalanceDtoCreateRequest(existingAccountName, idempotencyKey);

        Balance existingBalance = new Balance();
        existingBalance.setAccountName(existingAccountName);

        Mockito.when(balanceRepository.findByAccountName(existingAccountName)).thenReturn(Optional.of(existingBalance));

        assertThatThrownBy(() -> balanceService.createBalance(request))
                .isInstanceOf(BalanceWithNameAlreadyExistsException.class)
                .hasMessageContaining(existingAccountName);
    }

}
