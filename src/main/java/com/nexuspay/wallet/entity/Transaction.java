package com.nexuspay.wallet.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}