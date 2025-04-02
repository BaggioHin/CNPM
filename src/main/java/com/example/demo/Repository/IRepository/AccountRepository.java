package com.example.demo.Repository.IRepository;

import com.example.demo.Repository.Entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    Optional<AccountEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
