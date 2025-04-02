package com.example.demo.Service;

import com.example.demo.DTO.Request.MemberRequest;
import com.example.demo.DTO.Request.UpdateMemberRequest;
import com.example.demo.DTO.Response.MemberResponse;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Mapper.MemberMapper;
import com.example.demo.Repository.Entity.AccountEntity;
import com.example.demo.Repository.Entity.MemberEntity;
import com.example.demo.Repository.Entity.ReaderEntity;
import com.example.demo.Repository.IRepository.AccountRepository;
import com.example.demo.Repository.IRepository.MemberRepository;
import com.example.demo.Repository.IRepository.ReaderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReaderRepository readerRepository;

    @Autowired
    MemberMapper memberMapper;
    @Autowired
    private AccountRepository accountRepository;

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public MemberResponse createMember(MemberRequest memberRequest) {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        AccountEntity accountEntity = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        ReaderEntity reader = readerRepository.findById(accountEntity.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        for (MemberEntity member : reader.getMemberEntities()) {
            if (member.getExpiryTime() != null
                    && member.getExpiryTime().toInstant().isAfter(Instant.now())
                    && "Membership".equals(member.getStatus())) {
                throw new AppException(ErrorCode.ALREADY_ACTIVE_MEMBER);
            }
        }

        // Tạo thành viên mới
        MemberEntity memberEntity = memberMapper.toEntity(memberRequest);
        memberEntity.setReader(reader);

        memberRepository.save(memberEntity);

        return memberMapper.toResponse(memberEntity);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public MemberResponse updateMember(UpdateMemberRequest updateMemberRequest, Integer id) {
//        log.info("here1");
        // Tìm độc giả theo ID, nếu không có thì ném lỗi
        ReaderEntity readerEntity = readerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        log.info("here2");
        List<MemberEntity> pendingMembers = readerEntity.getMemberEntities().stream()
                .filter(member -> "Pending".equals(member.getStatus()))
                .sorted(Comparator.comparing(MemberEntity::getExpiryTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        if (pendingMembers.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
//        log.info("here3");
        MemberEntity latestMember = pendingMembers.get(0);

        latestMember.setStatus(updateMemberRequest.getStatus());

        return memberMapper.toResponse(memberRepository.save(latestMember));
    }

}
