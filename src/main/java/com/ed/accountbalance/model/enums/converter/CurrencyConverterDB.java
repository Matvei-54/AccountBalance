package com.ed.accountbalance.model.enums.converter;

import com.ed.accountbalance.model.enums.Currency;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyConverterDB implements AttributeConverter<Currency, String> {

    @Override
    public String convertToDatabaseColumn(Currency attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public Currency convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Currency.fromString(dbData);
    }
}
