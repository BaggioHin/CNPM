package com.example.demo.Repository.IRepository;

import com.example.demo.Repository.Entity.BookLendingEntity;
import com.example.demo.Repository.Entity.ReaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookLendingEntity,Integer> {
//    @Query("select f.fineAmount, from FineReceiptEntity f")
    List<BookLendingEntity> findByReader_id(int reader_id);
}

