package com.nexuspay.wallet.dto;

public class LoginDTO {
    private String email;
    private String password;

    public LoginDTO() {}

    // Getters y Setters Manuales
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}