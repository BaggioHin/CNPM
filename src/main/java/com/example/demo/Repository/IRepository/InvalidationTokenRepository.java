package com.example.demo.Repository.IRepository;

import com.example.demo.Repository.Entity.InvalidationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidationTokenRepository extends JpaRepository<InvalidationTokenEntity, String> {
}
