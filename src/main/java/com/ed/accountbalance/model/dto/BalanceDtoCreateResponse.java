package com.ed.accountbalance.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BalanceDtoCreateResponse {

    private String accountName;
    private String currency;
    private BigDecimal balance;
    private String accountStatus;
}
