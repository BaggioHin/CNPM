package com.example.demo.Repository.IRepository;

import com.example.demo.Repository.Entity.FineReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FineReceiptRepository extends JpaRepository<FineReceiptEntity, Integer> {
    Optional<FineReceiptEntity> findByReader_id(Integer id);
}
