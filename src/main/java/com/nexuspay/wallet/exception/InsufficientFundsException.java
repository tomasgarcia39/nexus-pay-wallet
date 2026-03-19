package com.nexuspay.wallet.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Saldo insuficiente para realizar la operación.");
    }
}