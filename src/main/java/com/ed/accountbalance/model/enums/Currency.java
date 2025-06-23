package com.ed.accountbalance.model.enums;

import lombok.Getter;

@Getter
public enum Currency {

    USD, EUR, BYN, RUB, GBP;

    public static Currency fromString(String currencyName) {
        for (Currency currency : Currency.values()) {
            if (currency.toString().equalsIgnoreCase(currencyName)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unknown currencyName: " + currencyName);
    }
}
