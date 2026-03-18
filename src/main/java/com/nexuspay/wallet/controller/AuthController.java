package com.nexuspay.wallet.controller;

import com.nexuspay.wallet.dto.UserDTO;
import com.nexuspay.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final WalletService walletService;

    // Inyección por constructor (Práctica recomendada Senior)
    public AuthController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDto) {
        try {
            walletService.registerUser(
                userDto.getEmail(), 
                userDto.getPassword(), 
                userDto.getFullName()
            );
            return ResponseEntity.ok("Usuario registrado exitosamente y cuenta vinculada.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar: " + e.getMessage());
        }
    }
}
