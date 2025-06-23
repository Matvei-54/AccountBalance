package com.ed.accountbalance.transaction;

import com.ed.accountbalance.exception.transaction.InsufficientFundsException;
import com.ed.accountbalance.mapper.TransactionMapper;
import com.ed.accountbalance.model.dto.TransactionDtoCreateRequest;
import com.ed.accountbalance.model.dto.TransactionDtoCreateResponse;
import com.ed.accountbalance.model.entity.Balance;
import com.ed.accountbalance.model.entity.Transaction;
import com.ed.accountbalance.model.enums.Currency;
import com.ed.accountbalance.model.enums.TransactionStatus;
import com.ed.accountbalance.model.enums.TransactionType;
import com.ed.accountbalance.repository.BalanceRepository;
import com.ed.accountbalance.repository.TransactionRepository;
import com.ed.accountbalance.service.IdempotencyService;
import com.ed.accountbalance.service.TransactionService;
import com.ed.accountbalance.service.converter.CurrencyConverter;
import com.ed.accountbalance.service.operation.DepositService;
import com.ed.accountbalance.service.operation.TransactionCommandHandler;
import com.ed.accountbalance.service.operation.WithdrawService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionOperationServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private CurrencyConverter currencyConverter;

    @Mock
    private IdempotencyService idempotencyService;

    @Mock
    private Map<TransactionType, TransactionCommandHandler> transactionsCommandHandlers;

    @InjectMocks
    private WithdrawService withdrawService;

    @InjectMocks
    private TransactionService transactionService;

    private final UUID TEST_UUID = UUID.randomUUID();
    private final String ACCOUNT_NAME = "test-account";
    private final String IDEMPOTENCY_KEY = "key-123";
    private final BigDecimal AMOUNT = new BigDecimal("100.00");

    @DisplayName("Успешное выполнение депозита на баланс.")
    @Test
    void createTransaction_Deposit_ReturnResponse() {
        // Given
        TransactionDtoCreateRequest request = new TransactionDtoCreateRequest(
                AMOUNT, ACCOUNT_NAME, IDEMPOTENCY_KEY);

        Balance balance = new Balance();
        balance.setAccountName(ACCOUNT_NAME);

        Transaction transaction = new Transaction();
        transaction.setId(TEST_UUID);
        transaction.setAmount(AMOUNT);
        transaction.setCurrency(Currency.USD);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setOwnerBalance(balance);

        TransactionDtoCreateResponse expectedResponse = new TransactionDtoCreateResponse(
                AMOUNT, Currency.USD.toString(), TransactionType.DEPOSIT.toString(), TransactionStatus.SUCCESS.toString());

        TransactionCommandHandler handler = Mockito.mock(DepositService.class);

        Mockito.when(balanceRepository.findByAccountNameWithLock(ACCOUNT_NAME))
                .thenReturn(Optional.of(balance));
        Mockito.when(transactionsCommandHandlers.get(TransactionType.DEPOSIT))
                .thenReturn(handler);
        Mockito.when(handler.process(AMOUNT, balance, Currency.USD))
                .thenReturn(transaction);
        Mockito.when(transactionMapper.transactionToDtoCreate(transaction))
                .thenReturn(expectedResponse);

        // When
        TransactionDtoCreateResponse response = transactionService.createTransaction(
                request, TransactionType.DEPOSIT, Currency.USD);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        Mockito.verify(idempotencyService).saveIdempotencyKey(IDEMPOTENCY_KEY, expectedResponse, 3600);
    }


    @DisplayName("Успешное выполнение вывода средств с баланса.")
    @Test
    void createTransaction_Withdrawal_ShouldReturnResponse() {
        // Given
        TransactionDtoCreateRequest request = new TransactionDtoCreateRequest(
                AMOUNT, ACCOUNT_NAME, IDEMPOTENCY_KEY);

        Balance balance = new Balance();
        balance.setAccountName(ACCOUNT_NAME);

        Transaction transaction = new Transaction();
        transaction.setId(TEST_UUID);
        transaction.setAmount(AMOUNT);
        transaction.setCurrency(Currency.EUR);
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setOwnerBalance(balance);

        TransactionDtoCreateResponse expectedResponse = new TransactionDtoCreateResponse(
                AMOUNT, Currency.USD.toString(), TransactionType.DEPOSIT.toString(), TransactionStatus.SUCCESS.toString());

        TransactionCommandHandler handler = Mockito.mock(WithdrawService.class);

        Mockito.when(balanceRepository.findByAccountNameWithLock(ACCOUNT_NAME))
                .thenReturn(Optional.of(balance));
        Mockito.when(transactionsCommandHandlers.get(TransactionType.WITHDRAWAL))
                .thenReturn(handler);
        Mockito.when(handler.process(AMOUNT, balance, Currency.EUR))
                .thenReturn(transaction);
        Mockito.when(transactionMapper.transactionToDtoCreate(transaction))
                .thenReturn(expectedResponse);

        // When
        TransactionDtoCreateResponse response = transactionService.createTransaction(
                request, TransactionType.WITHDRAWAL, Currency.EUR);

        // Then
        assertThat(response).isEqualTo(expectedResponse);
        Mockito.verify(idempotencyService).saveIdempotencyKey(IDEMPOTENCY_KEY, expectedResponse, 3600);
    }

    @DisplayName("Вывод средств, возвращает ошибку - недостаточно средств.")
    @Test
    void process_WhenInsufficientFunds_ShouldThrowException() {

        BigDecimal CURRENT_BALANCE = new BigDecimal("100.00");
        BigDecimal WITHDRAWAL_AMOUNT = new BigDecimal("150.00");
        BigDecimal CONVERTED_AMOUNT = new BigDecimal("120.00");
        Currency CURRENCY = Currency.EUR;

        // Given
        Balance balance = new Balance();
        balance.setBalance(CURRENT_BALANCE);
        balance.setCurrency(Currency.USD);

        Mockito.when(currencyConverter.convert(CURRENCY.toString(), WITHDRAWAL_AMOUNT))
                .thenReturn(CONVERTED_AMOUNT);

        assertThatThrownBy(() -> withdrawService.process(WITHDRAWAL_AMOUNT, balance, CURRENCY))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining(WITHDRAWAL_AMOUNT.toString());
    }

}
