package com.example.demo.Service;

import com.example.demo.DTO.Request.ChangePasswordRequest;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.Entity.AccountEntity;
import com.example.demo.Repository.Entity.ReaderEntity;
import com.example.demo.Repository.IRepository.AccountRepository;
import com.example.demo.Repository.IRepository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ReaderRepository readerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PreAuthorize("isAuthenticated() and hasAuthority('SCOPE_ADMIN')")
    public List<ReaderEntity> getAll(){
        return readerRepository.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    public String  changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (Objects.equals(changePasswordRequest.getNewPassword(), changePasswordRequest.getOldPassword())) {
            throw new AppException(ErrorCode.INVALID_NEW_PASSWORD);
        }

        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        AccountEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), account.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }
        account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        accountRepository.save(account);
        return account.getPassword();
    }
}
