package com.example.demo.Controller;

import com.example.demo.DTO.Response.ApiResponse;
import com.example.demo.DTO.Response.BookLendingResponse;
import com.example.demo.Service.BookLendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookLendingController {

    @Autowired
    BookLendingService bookLendingService;

    @GetMapping("/{id}")
    public ApiResponse<List<BookLendingResponse>> getBookLending(@PathVariable Integer id) {
        return ApiResponse.<List<BookLendingResponse>>builder()
                .result(bookLendingService.getBookById(id))
                .build();
    }
}
