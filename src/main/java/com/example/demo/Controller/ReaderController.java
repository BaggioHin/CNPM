package com.example.demo.Controller;

import com.example.demo.DTO.Request.ReaderRequest;
import com.example.demo.DTO.Response.ApiResponse;
import com.example.demo.DTO.Response.ReaderResponse;
import com.example.demo.Service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reader")
public class ReaderController {

    @Autowired
    ReaderService readerService;

    @GetMapping
    public ApiResponse<List<ReaderResponse>> getAllReader() {
        return ApiResponse.<List<ReaderResponse>>builder()
                .result(readerService.getAllReader())
                .build();
    }

    @GetMapping("/user")
    public ApiResponse<ReaderResponse> getReader() {
        var reader  = readerService.getReaderById();
        return ApiResponse.<ReaderResponse>builder()
                .result(reader)
                .build();
    }

    @GetMapping("/{name}")
    public ApiResponse<ReaderResponse> getReaderById(@PathVariable String name) {
        var reader  = readerService.getReaderByName(name);
        return ApiResponse.<ReaderResponse>builder()
                .result(reader)
                .build();
    }

    @PostMapping
    public ApiResponse<ReaderResponse> createReader(@RequestBody ReaderRequest readerRequest) {
        return ApiResponse.<ReaderResponse>builder()
                .result(readerService.createReader(readerRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ReaderResponse> updateReader(@PathVariable Integer id, @RequestBody ReaderRequest readerRequest) {
        return ApiResponse.<ReaderResponse>builder()
                .result(readerService.updateReader(id,readerRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReader(@PathVariable Integer id) {
        readerService.deleteReader(id);
    }
}
