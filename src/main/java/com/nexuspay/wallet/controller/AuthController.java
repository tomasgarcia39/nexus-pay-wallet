package com.nexuspay.wallet.controller;

import com.nexuspay.wallet.dto.LoginDTO;
import com.nexuspay.wallet.dto.UserDTO;
import com.nexuspay.wallet.dto.UserResponseDTO;
import com.nexuspay.wallet.entity.User;
import com.nexuspay.wallet.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserDTO userDto) {
        User user = authService.register(userDto);
        UserResponseDTO response = new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getAccount().getAccountNumber()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginDTO loginDto) {
        User user = authService.login(loginDto);
        UserResponseDTO response = new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getAccount().getAccountNumber()
        );
        return ResponseEntity.ok(response);
    }
}