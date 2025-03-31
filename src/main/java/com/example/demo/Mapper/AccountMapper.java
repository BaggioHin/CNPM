package com.example.demo.Mapper;

import com.example.demo.DTO.Request.ReaderRequest;
import com.example.demo.Repository.Entity.AccountEntity;
import com.example.demo.Repository.Entity.ReaderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AccountMapper {
//    @Mapping(target = "phone",ignore = true)
//    @Mapping(target = "address",ignore = true)
    AccountEntity toAccountEntity(ReaderRequest readerRequest);
}
