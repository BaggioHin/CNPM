package com.example.demo.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FineReceiptRequest {
    private Integer bookId;
    private Double amount;
    private String status="PENDING";
    private String note;
}
