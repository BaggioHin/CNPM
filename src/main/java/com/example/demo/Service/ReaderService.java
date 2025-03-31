package com.example.demo.Service;

import com.example.demo.DTO.Request.ReaderRequest;
import com.example.demo.DTO.Response.ReaderResponse;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Mapper.ReaderMapper;
import com.example.demo.Repository.Entity.*;
import com.example.demo.Repository.IRepository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class ReaderService {
    @Autowired
    ReaderRepository readerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ReaderMapper readerMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<ReaderResponse> getAllReader() {
        List<ReaderEntity> readers = readerRepository.findAll();

        if (readers.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        List<ReaderResponse> responses = new ArrayList<>();

        for (ReaderEntity reader : readers) {
            ReaderEntity readerEntity = readerRepository.findById(reader.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            List<MemberEntity> members = readerEntity.getMemberEntities();

            // Lấy trạng thái của MemberEntity đã hết hạn (nếu có)
            String status = members.stream()
                    .filter(member -> member.getExpiryTime() == null || member.getExpiryTime().toInstant().isAfter(Instant.now()))
                    .sorted(Comparator.comparing(MemberEntity::getExpiryTime, Comparator.nullsLast(Comparator.reverseOrder())))
                    .map(MemberEntity::getStatus)
                    .findFirst()
                    .orElse("NonMembership");


            // Tạo đối tượng ReaderResponse
            ReaderResponse readerResponse = ReaderResponse.builder()
                    .id(reader.getId())
                    .status(status)
                    .address(reader.getAddress())
                    .name(reader.getName())
                    .phone(reader.getPhone())
                    .build();

            responses.add(readerResponse);
        }

        return responses;
    }



    @PreAuthorize("hasAuthority('SCOPE_USER') and isAuthenticated()")
    public ReaderResponse getReaderById() {
        var context = SecurityContextHolder.getContext();
        String username =  context.getAuthentication().getName();
        AccountEntity accountEntity = accountRepository.findByUsername(username).get();

        ReaderEntity readerEntity = readerRepository.findById(accountEntity.getId()).get();
        return readerMapper.toResponse(readerEntity);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ReaderResponse getReaderByName(String name) {
        ReaderEntity readerEntity = readerRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return readerMapper.toResponse(readerEntity);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ReaderResponse createReader(ReaderRequest readerRequest) {
        // Kiểm tra username đã tồn tại chưa
        if (accountRepository.findByUsername(readerRequest.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Mã hóa password
        String encodedPassword = passwordEncoder.encode(readerRequest.getPassword());

        // Tạo AccountEntity với đầy đủ thông tin
        AccountEntity account = AccountEntity.builder()
                .username(readerRequest.getUsername())
                .password(encodedPassword)
                .role("USER")
                .email(readerRequest.getEmail())
                .note(readerRequest.getNote())
                .build();

        // Lưu account để sinh ra ID
        accountRepository.saveAndFlush(account);

        // Tạo ReaderEntity với ID từ account
        ReaderEntity reader = ReaderEntity.builder()
                .id(account.getId())
                .name(readerRequest.getName())
                .phone(readerRequest.getPhone())
                .address(readerRequest.getAddress())
                .build();

        // Lưu reader
        readerRepository.save(reader);

        return readerMapper.toResponse(reader);
    }

    @Transactional
    public ReaderResponse updateReader(Integer id, ReaderRequest reader) {
        if(readerRepository.findById(id).isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        };
        ReaderEntity readerEntity = readerRepository.findById(id).get();
        readerEntity = readerMapper.toEntity(reader, readerEntity);
        return readerMapper.toResponse(readerRepository.save(readerEntity));
    }

    @Transactional
    public void deleteReader(Integer id) {
        readerRepository.deleteById(id);
        accountRepository.deleteById(id);
    }
}
