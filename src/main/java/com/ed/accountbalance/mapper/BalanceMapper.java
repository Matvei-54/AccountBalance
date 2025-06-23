package com.ed.accountbalance.mapper;

import com.ed.accountbalance.model.dto.BalanceDtoCreateResponse;
import com.ed.accountbalance.model.entity.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

//    @Mapping(target = "accountStatus", source = "balance.accountStatus")
//    BalanceDtoCreateResponse balanceToDto(Balance balance);

    @Mapping(target = "accountStatus", source = "balance.accountStatus")
    BalanceDtoCreateResponse balanceToDto(Balance balance);
}
