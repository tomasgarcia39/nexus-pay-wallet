package com.nexuspay.wallet.dto;

import java.math.BigDecimal;

public class TransferRequestDTO {

    private Long senderUserId;
    private String receiverAccountNumber;
    private BigDecimal amount;

    public TransferRequestDTO() {}

    public Long getSenderUserId() { return senderUserId; }
    public void setSenderUserId(Long senderUserId) { this.senderUserId = senderUserId; }

    public String getReceiverAccountNumber() { return receiverAccountNumber; }
    public void setReceiverAccountNumber(String receiverAccountNumber) { this.receiverAccountNumber = receiverAccountNumber; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}