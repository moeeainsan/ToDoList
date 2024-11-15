package com.springproject.springboot.security.project.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springproject.springboot.security.project.demo.entity.User;
import com.springproject.springboot.security.project.demo.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RestControllerAdvice
@RequestMapping("/api/")
@RequiredArgsConstructor

public class AuthenticationController {


    private final AuthenticationService service;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register
    (@Valid @RequestBody User request) {
        return ResponseEntity.ok(service.register(request));
    }

    
    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate
    ( @Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

  

    
    
    
}
