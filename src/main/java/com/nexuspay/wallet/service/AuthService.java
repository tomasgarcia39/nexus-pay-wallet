package com.nexuspay.wallet.service;

import com.nexuspay.wallet.dto.LoginDTO;
import com.nexuspay.wallet.dto.UserDTO;
import com.nexuspay.wallet.entity.User;
import com.nexuspay.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    @Transactional
    public void register(UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        userRepository.save(user);
        walletService.createAccountForUser(user);
    }

    public User login(LoginDTO loginDto) {
        // Buscamos al usuario o lanzamos error si no existe
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificamos la contraseña (texto plano por ahora)
        if (!user.getPassword().equals(loginDto.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return user;
    }
}