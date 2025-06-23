package com.ed.accountbalance.model.entity;

import com.ed.accountbalance.model.enums.AccountStatus;
import com.ed.accountbalance.model.enums.Currency;
import com.ed.accountbalance.model.enums.converter.AccountStatusConverter;
import com.ed.accountbalance.model.enums.converter.CurrencyConverterDB;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "balance")
@Entity
@NoArgsConstructor
@Setter
@Getter
public class Balance extends BaseEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_balance")
    @SequenceGenerator(name = "sequence_balance", sequenceName = "balance_main_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "balance_name")
    private String accountName;

    @Convert(converter = CurrencyConverterDB.class)
    private Currency currency;

    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "balance_status")
    @Convert(converter = AccountStatusConverter.class)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "ownerBalance", cascade = CascadeType.ALL)
    private List<Transaction> transactionsHistory = new ArrayList<>();

}
