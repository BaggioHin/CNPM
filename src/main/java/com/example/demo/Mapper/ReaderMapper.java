package com.example.demo.Mapper;

import com.example.demo.DTO.Request.ReaderRequest;
import com.example.demo.DTO.Response.ReaderResponse;
import com.example.demo.Repository.Entity.ReaderEntity;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ReaderMapper {
    @Mapping(target = "id", source = "id")
    ReaderResponse toResponse(ReaderEntity reader);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReaderEntity toEntity(ReaderRequest reader,@MappingTarget ReaderEntity readerEntity);

//    @Mapping(source = "phone", target = "phone")
//    @Mapping(source = "address",target = "address")
//    ReaderEntity toReaderEntity(ReaderRequest reader,@MappingTarget ReaderEntity readerEntity);
}
