package com.nexuspay.wallet.controller;

import com.nexuspay.wallet.dto.TransferRequestDTO;
import com.nexuspay.wallet.entity.Account;
import com.nexuspay.wallet.entity.Transaction;
import com.nexuspay.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

 
    @GetMapping("/{userId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long userId) {
        Account account = walletService.getAccountByUserId(userId);
        BigDecimal available = walletService.getAvailableBalance(userId);

        return ResponseEntity.ok(new java.util.HashMap<>() {{
            put("accountNumber", account.getAccountNumber());
            put("balance", account.getBalance());
            put("reservedBalance", account.getReservedBalance());
            put("availableBalance", available);
        }});
    }

   
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequestDTO dto) {
        walletService.transfer(
                dto.getSenderUserId(),
                dto.getReceiverAccountNumber(),
                dto.getAmount()
        );
        return ResponseEntity.ok("Transferencia realizada exitosamente.");
    }

   
    @GetMapping("/{userId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long userId) {
        List<Transaction> transactions = walletService.getTransactionHistory(userId);
        return ResponseEntity.ok(transactions);
    }
}