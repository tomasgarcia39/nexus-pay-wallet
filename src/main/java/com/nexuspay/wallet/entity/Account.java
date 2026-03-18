package com.nexuspay.wallet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal reservedBalance = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Account() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public BigDecimal getReservedBalance() { return reservedBalance; }
    public void setReservedBalance(BigDecimal reservedBalance) { this.reservedBalance = reservedBalance; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}