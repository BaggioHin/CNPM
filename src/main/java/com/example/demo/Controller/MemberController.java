package com.example.demo.Controller;

import com.example.demo.DTO.Request.MemberRequest;
import com.example.demo.DTO.Request.UpdateMemberRequest;
import com.example.demo.DTO.Response.ApiResponse;
import com.example.demo.DTO.Response.MemberResponse;
import com.example.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @PostMapping("/create")
    public ApiResponse<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.createMember(memberRequest))
                .build();
    }

    @PostMapping("/update/{id}")
    public ApiResponse<MemberResponse> updateMember(@RequestBody UpdateMemberRequest updateMemberRequest
    , @PathVariable int id) {
        return ApiResponse.<MemberResponse>builder()
                .result(memberService.updateMember(updateMemberRequest,id))
                .build();
    }
}
