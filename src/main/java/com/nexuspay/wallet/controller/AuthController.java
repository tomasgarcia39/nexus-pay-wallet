package com.nexuspay.wallet.controller;

import com.nexuspay.wallet.dto.LoginDTO;
import com.nexuspay.wallet.dto.UserDTO;
import com.nexuspay.wallet.entity.User;
import com.nexuspay.wallet.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDto) {
        try {
            authService.register(userDto);
            return ResponseEntity.ok("Usuario registrado exitosamente y cuenta vinculada.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
            User user = authService.login(loginDto);
            return ResponseEntity.ok("Login exitoso. Bienvenido " + user.getFullName());
        } catch (RuntimeException e) {
            // 401 Unauthorized es el código correcto para fallos de login
            return ResponseEntity.status(401).body("Error de autenticación: " + e.getMessage());
        }
    }
}