package com.example.demo.Mapper;

import com.example.demo.DTO.Request.MemberRequest;
import com.example.demo.DTO.Response.MemberResponse;
import com.example.demo.Repository.Entity.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface MemberMapper {
    @Mapping(target="reader",ignore = true)
    @Mapping(source="expiryDate",target = "expiryTime")
    MemberEntity toEntity(MemberRequest memberRequest);

    @Mapping(source="expiryTime",target = "expiryDate")
    MemberResponse toResponse(MemberEntity memberEntity);
//    @Mapping(target="expiryDate",source = "expiryDate")
//    MemberResponse toResponse( MemberRequest memberRequest);
}
