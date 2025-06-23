package com.ed.accountbalance.transaction;

import com.ed.accountbalance.exception.balance.BalanceWithNameNoExistsException;
import com.ed.accountbalance.mapper.TransactionMapper;
import com.ed.accountbalance.model.dto.TransactionDtoResponse;
import com.ed.accountbalance.model.entity.Balance;
import com.ed.accountbalance.model.entity.Transaction;
import com.ed.accountbalance.model.enums.Currency;
import com.ed.accountbalance.model.enums.TransactionType;
import com.ed.accountbalance.repository.BalanceRepository;
import com.ed.accountbalance.repository.TransactionRepository;
import com.ed.accountbalance.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;


    @DisplayName("Получение списка транзакций в виде Page")
    @Test
    void getTransactions_WhenBalanceExists_ReturnPageOfTransactions() {
        // Given
        String balanceName = "test-account";
        int page = 0;
        int size = 10;
        Long balanceId = 1L;

        Balance balance = new Balance();
        balance.setId(balanceId);
        balance.setAccountName(balanceName);

        Transaction transaction = new Transaction();
        transaction.setOwnerBalance(balance);

        TransactionDtoResponse dtoResponse = new TransactionDtoResponse(
                Currency.USD.toString(), new BigDecimal(100.00), TransactionType.DEPOSIT.toString(),
                "USD", "DEPOSIT");

        Page<Transaction> transactionPage = new PageImpl<>(Collections.singletonList(transaction));
        Page<TransactionDtoResponse> expectedPage = new PageImpl<>(Collections.singletonList(dtoResponse));

        Mockito.when(balanceRepository.findByAccountName(balanceName)).thenReturn(Optional.of(balance));
        Mockito.when(transactionRepository.findByOwnerBalanceId(balanceId, PageRequest.of(page, size)))
                .thenReturn(transactionPage);
        Mockito.when(transactionMapper.transactionToDto(transaction)).thenReturn(dtoResponse);

        // When
        Page<TransactionDtoResponse> result = transactionService.getTransactions(balanceName, page, size);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(dtoResponse);

        Mockito.verify(balanceRepository).findByAccountName(balanceName);
        Mockito.verify(transactionRepository).findByOwnerBalanceId(balanceId, PageRequest.of(page, size));
        Mockito.verify(transactionMapper).transactionToDto(transaction);
    }


    @DisplayName("Получение списка транзакций, возврат ошибки")
    @Test
    void getTransactions_WhenBalanceNotExists_ReturnThrowException() {
        // Given
        String nonExistentBalanceName = "non-existent-account";
        int page = 0;
        int size = 10;

        Mockito.when(balanceRepository.findByAccountName(nonExistentBalanceName)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> transactionService.getTransactions(nonExistentBalanceName, page, size))
                .isInstanceOf(BalanceWithNameNoExistsException.class)
                .hasMessageContaining(nonExistentBalanceName);
    }

}
