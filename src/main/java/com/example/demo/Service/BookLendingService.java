package com.example.demo.Service;

import com.example.demo.DTO.Response.BookLendingResponse;
import com.example.demo.Repository.Entity.BookLendingEntity;
import com.example.demo.Repository.IRepository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookLendingService {

    @Autowired
    BookingRepository bookingRepository;

    public List<BookLendingResponse> getBookById(Integer id){
        List<BookLendingEntity> bookLendingEntities = bookingRepository.findByReader_id(id);
        List<BookLendingResponse> bookLendingResponses = new ArrayList<>();
        for(BookLendingEntity bookLendingEntity : bookLendingEntities){
            BookLendingResponse bookLendingResponse = BookLendingResponse.builder()
                    .borrowDate(bookLendingEntity.getBorrowDate())
                    .expiryDate(bookLendingEntity.getExpiryDate())
                    .status(bookLendingEntity.getStatus())
                    .countBooks(bookLendingEntity.getCountBooks())
                    .build();
            bookLendingResponses.add(bookLendingResponse);
        }
        return bookLendingResponses;
    }
}
