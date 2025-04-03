package com.example.demo.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReaderResponse {
    private Integer id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String username;
    private String note;
    private String status;
}
