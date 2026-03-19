package com.nexuspay.wallet.service;

import com.nexuspay.wallet.dto.LoginDTO;
import com.nexuspay.wallet.dto.UserDTO;
import com.nexuspay.wallet.entity.User;
import com.nexuspay.wallet.exception.EmailAlreadyExistsException;
import com.nexuspay.wallet.exception.UserNotFoundException;
import com.nexuspay.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;

    // BCrypt para encriptar contraseñas — nunca guardar texto plano
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public User register(UserDTO dto) {
        // Usamos nuestra excepción custom en lugar de RuntimeException
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        // La contraseña se guarda encriptada
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
        walletService.createAccountForUser(user);

        return user;
    }

    public User login(LoginDTO loginDto) {
        // Si no existe el email, lanzamos excepción custom
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(loginDto.getEmail()));

        // BCrypt compara el texto plano con el hash guardado
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

        return user;
    }
}