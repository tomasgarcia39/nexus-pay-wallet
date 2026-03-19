package com.nexuspay.wallet.dto;

import java.math.BigDecimal;


public class ReservationRequestDTO {

    private Long userId;
    private BigDecimal amount;
    private String description;

    public ReservationRequestDTO() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}