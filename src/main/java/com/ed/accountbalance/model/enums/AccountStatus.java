package com.ed.accountbalance.model.enums;

import lombok.Getter;

@Getter
public enum AccountStatus {

    ACTIVE,BLOCKED,EXPIRED;

    public static AccountStatus fromString(String accountStatus) {
        for (AccountStatus status : AccountStatus.values()) {
            if(status.toString().equals(accountStatus)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid account status: " + accountStatus);
    }
}
