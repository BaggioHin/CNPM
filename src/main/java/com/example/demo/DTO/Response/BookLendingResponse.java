package com.example.demo.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLendingResponse {
    private Date borrowDate;
    private Date expiryDate;
    private String status;
    private Integer countBooks;
}
