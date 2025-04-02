package com.example.demo.DTO.Response;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class InfReaderResponse {
    private Integer id;
    private ReaderResponse readerResponse;
    private BookLendingResponse bookLendingResponse;
    private FineReceiptResponse fineReceiptResponse;
}
