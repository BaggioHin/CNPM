package com.example.demo.Controller;

import com.example.demo.DTO.Request.ChangePasswordRequest;
import com.example.demo.DTO.Request.LogoutRequest;
import com.example.demo.DTO.Response.ApiResponse;
import com.example.demo.DTO.Response.AuthenticationResponse;
import com.example.demo.DTO.Request.AuthenticationRequest;
import com.example.demo.Service.AccountService;
import com.example.demo.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> Login(@RequestBody AuthenticationRequest loginRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(loginRequest))
                .build();
    }

    @PostMapping("/logout")
    public void Logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
    }

    @PostMapping("/user")
    public ApiResponse<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return ApiResponse.<String>builder()
                .result(accountService.changePassword(changePasswordRequest))
                .build();
    }
}
