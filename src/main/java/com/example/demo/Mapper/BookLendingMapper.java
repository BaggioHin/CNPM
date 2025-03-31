package com.example.demo.Mapper;

import com.example.demo.DTO.Response.BookLendingResponse;
import com.example.demo.DTO.Response.InfReaderResponse;
import com.example.demo.Repository.Entity.BookLendingEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface BookLendingMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BookLendingResponse toResponse(BookLendingEntity bookLendingEntity);
}
