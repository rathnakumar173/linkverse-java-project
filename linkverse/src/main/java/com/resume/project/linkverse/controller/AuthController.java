package com.resume.project.linkverse.controller;

import com.resume.project.linkverse.dto.AuthDtos;
import com.resume.project.linkverse.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthDtos.UserDto> registerUser(@Valid @RequestBody AuthDtos.RegisterRequestDto requestDto) {
        AuthDtos.UserDto newUser = authService.registerUser(requestDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthDtos.AuthResponseDto> loginUser(@Valid @RequestBody AuthDtos.LoginRequestDto requestDto) {
        AuthDtos.AuthResponseDto response = authService.loginUser(requestDto);
        return ResponseEntity.ok(response);
    }
}