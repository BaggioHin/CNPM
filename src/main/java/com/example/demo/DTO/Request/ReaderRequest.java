package com.example.demo.DTO.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReaderRequest {
    private String username;
    private String password;
    private String phone;
    private String address;
    private String name;
    private String email;
    private String role="USER";
    private String note;
    @JsonProperty("membership")
    private String status;
    private Date expireDate;
    private Date membershipDate;
}
