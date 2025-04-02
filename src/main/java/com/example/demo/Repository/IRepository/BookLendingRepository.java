package com.example.demo.Repository.IRepository;

import com.example.demo.Repository.Entity.BookLendingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookLendingRepository extends JpaRepository<BookLendingEntity,Integer> {
//    @Query("select f.fineAmount, from FineReceiptEntity f")
    List<BookLendingEntity> findByReader_id(int reader_id);
}

