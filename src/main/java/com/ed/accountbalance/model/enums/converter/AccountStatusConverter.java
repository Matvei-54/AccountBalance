package com.ed.accountbalance.model.enums.converter;

import com.ed.accountbalance.model.enums.AccountStatus;
import com.ed.accountbalance.model.enums.Currency;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountStatusConverter implements AttributeConverter<AccountStatus, String> {

    @Override
    public String convertToDatabaseColumn(AccountStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public AccountStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AccountStatus.fromString(dbData);
    }
}
