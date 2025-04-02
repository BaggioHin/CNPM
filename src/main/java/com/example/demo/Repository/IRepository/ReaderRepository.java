package com.example.demo.Repository.IRepository;

import com.example.demo.Repository.Entity.ReaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<ReaderEntity, Integer> {
    Optional<ReaderEntity> findById(Integer id);
    Optional<ReaderEntity> findByName(String name);
}
