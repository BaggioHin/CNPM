package com.example.demo.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class createReaderRequest {
    String username;
    String password;
    String email;
    String note;
    String role = "USER";
}
