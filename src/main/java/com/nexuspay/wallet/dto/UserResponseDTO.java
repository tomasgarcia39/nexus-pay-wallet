package com.nexuspay.wallet.dto;


public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String accountNumber;

    public UserResponseDTO(Long id, String fullName, String email, String accountNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.accountNumber = accountNumber;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getAccountNumber() { return accountNumber; }
}