package com.ed.accountbalance.model.entity;

import com.ed.accountbalance.model.enums.Currency;
import com.ed.accountbalance.model.enums.TransactionStatus;
import com.ed.accountbalance.model.enums.TransactionType;
import com.ed.accountbalance.model.enums.converter.CurrencyConverterDB;
import com.ed.accountbalance.model.enums.converter.TransactionStatusConverter;
import com.ed.accountbalance.model.enums.converter.TransactionTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "transaction")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Transaction extends BaseEntity {

    @Id
    @Column(name = "id")
    private UUID id = UUID.randomUUID();

    @Column(name = "amount")
    private BigDecimal amount;

    @Convert(converter = CurrencyConverterDB.class)
    private Currency currency;

    @Column(name = "transaction_type")
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType transactionType;

    @Column(name = "transaction_status")
    @Convert(converter = TransactionStatusConverter.class)
    private TransactionStatus transactionStatus;

    @ManyToOne
    @JoinColumn(name = "owner_balance_id", referencedColumnName = "id")
    private Balance ownerBalance;
}
