package com.example.demo.Controller;

import com.example.demo.DTO.Request.FineReceiptRequest;
import com.example.demo.DTO.Response.ApiResponse;
import com.example.demo.DTO.Response.FineReceiptResponse;
import com.example.demo.Service.FineReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/User")
public class FineReceiptController {

    @Autowired
    FineReceiptService fineReceiptService;

    @PostMapping("/FineReceipt/{id}")
    public ApiResponse<FineReceiptResponse> createFineReceipt(@RequestBody  FineReceiptRequest request,
                                                              @PathVariable Integer id) {
        return ApiResponse.<FineReceiptResponse>builder()
                .result(fineReceiptService.createFineReceipt(request,id))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<List<FineReceiptResponse>> getFineReceipt(@PathVariable Integer id) {
        return ApiResponse.<List<FineReceiptResponse>>builder()
                .result(fineReceiptService.getFineReceipt(id))
                .build();
    }

    @GetMapping("/fineReceipt/{id}")
    public ApiResponse<FineReceiptResponse> getFineReceiptById(@PathVariable Integer id) {
        return ApiResponse.<FineReceiptResponse>builder()
                .result(fineReceiptService.getById(id))
                .build();
    }

    @GetMapping("/pay/{id}")
    public ApiResponse<FineReceiptResponse> payFineReceipt(@PathVariable Integer id) {
        return ApiResponse.<FineReceiptResponse>builder()
                .result(fineReceiptService.payFineReceipt(id))
                .build();
    }
}
