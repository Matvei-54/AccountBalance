package com.ed.accountbalance.mapper;

import com.ed.accountbalance.model.dto.TransactionDtoCreateResponse;
import com.ed.accountbalance.model.dto.TransactionDtoResponse;
import com.ed.accountbalance.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "transactionType", source = "transaction.transactionType")
    @Mapping(target = "transactionStatus", source = "transaction.transactionStatus")
    TransactionDtoCreateResponse transactionToDtoCreate(Transaction transaction);

    @Mapping(target = "balanceName", source = "transaction.ownerBalance.accountName")
    TransactionDtoResponse transactionToDto(Transaction transaction);
}
