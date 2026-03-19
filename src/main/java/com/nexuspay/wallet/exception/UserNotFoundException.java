package com.nexuspay.wallet.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("Usuario no encontrado con email: " + email);
    }
    public UserNotFoundException(Long id) {
        super("Usuario no encontrado con id: " + id);
    }
}