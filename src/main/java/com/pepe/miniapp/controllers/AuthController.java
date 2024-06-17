package com.pepe.miniapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pepe.miniapp.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody String payload, HttpServletRequest request) throws IOException {
        return authService.authenticate(payload, request);
    }
}
