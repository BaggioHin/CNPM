package com.example.demo.Mapper;

import com.example.demo.DTO.Request.FineReceiptRequest;
import com.example.demo.DTO.Response.FineReceiptResponse;
import com.example.demo.DTO.Response.InfReaderResponse;
import com.example.demo.Repository.Entity.FineReceiptEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface FineReceiptMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    FineReceiptResponse toResponse(FineReceiptEntity fineReceipt);

    FineReceiptEntity toEntity(FineReceiptRequest fineReceiptRequest);
}
