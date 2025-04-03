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
import java.time.ZoneId;
import java.util.*;
import java.time.LocalDateTime;

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
    @Autowired
    private MemberRepository memberRepository;

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
        String username = context.getAuthentication().getName();
        AccountEntity accountEntity = accountRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        ReaderEntity readerEntity = readerRepository.findById(accountEntity.getId()).orElseThrow(() -> new RuntimeException("Reader not found"));

        // Lấy status của biên lai có expiryTime muộn nhất
        String latestStatus = readerEntity.getMemberEntities().stream()
                .max(Comparator.comparing(MemberEntity::getExpiryTime)) // Lấy member có expiryTime lớn nhất
                .map(MemberEntity::getStatus) // Lấy status
                .orElse("NonMembership");


        return ReaderResponse.builder()
                .id(readerEntity.getId())
                .name(readerEntity.getName())
                .phone(readerEntity.getPhone())
                .address(readerEntity.getAddress())
                .username(username)
                .email(accountEntity.getEmail())
                .note(accountEntity.getNote())
                .status(latestStatus)
                .build();
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
        if(accountRepository.existsByUsername(readerRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
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
        System.out.println("here1");
        ReaderEntity readerEntity = readerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (reader.getEmail() != null && !reader.getEmail().trim().isEmpty()
                && !reader.getEmail().equals(accountEntity.getEmail())) {
            if (accountRepository.existsByEmail(reader.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_EXISTED);
            }
            accountEntity.setEmail(reader.getEmail());
            accountRepository.save(accountEntity);
            System.out.println("here2");
        }

        List<MemberEntity> existingMembers = readerEntity.getMemberEntities();
        readerEntity = readerMapper.toEntity(reader, readerEntity);
        readerEntity.setMemberEntities(existingMembers != null ? existingMembers : new ArrayList<>());
        readerRepository.save(readerEntity);

        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        if (Objects.equals(reader.getStatus(), "Membership")) {
            System.out.println("here4");
            Optional<MemberEntity> latestMember = readerEntity.getMemberEntities().stream()
                    .max(Comparator.comparing(MemberEntity::getExpiryTime));

            if (latestMember.isPresent() && latestMember.get().getExpiryTime().after(now)) {
                throw new AppException(ErrorCode.ALREADY_ACTIVE_MEMBER);
            }

            if (reader.getExpireDate() == null || reader.getMembershipDate() == null) {
                throw new AppException(ErrorCode.INVALID_DATE);
            }
            if (reader.getExpireDate().before(reader.getMembershipDate())) {
                throw new AppException(ErrorCode.INVALID_DATE_RANGE);
            }
            System.out.println("here3");
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setVersion(0);
            memberEntity.setStatus("Membership");
            memberEntity.setExpiryTime(reader.getExpireDate());
            memberEntity.setMembershipDate(reader.getMembershipDate());
            memberEntity.setReader(readerEntity);

            System.out.println("Before saving MemberEntity: " + memberEntity);
            MemberEntity savedEntity = memberRepository.saveAndFlush(memberEntity);
            System.out.println("Saved MemberEntity ID: " + savedEntity.getId() + "**********************");

            if (!readerEntity.getMemberEntities().contains(savedEntity)) {
                readerEntity.getMemberEntities().add(savedEntity);
            }
            readerRepository.saveAndFlush(readerEntity);
        }

        return readerMapper.toResponse(readerEntity);
    }

    @Transactional
    public void deleteReader(Integer id) {
        readerRepository.deleteById(id);
        accountRepository.deleteById(id);
    }
}
