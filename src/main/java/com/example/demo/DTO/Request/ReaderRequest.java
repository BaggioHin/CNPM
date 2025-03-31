package com.example.demo.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReaderRequest {
    private String phone;
    private String address;
    private String name;
    private String email;
    private String username;
    private String role="USER";
    private String note;
    private String password;
}
