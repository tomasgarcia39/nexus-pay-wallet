package com.nexuspay.wallet.service;

import com.nexuspay.wallet.entity.Account;
import com.nexuspay.wallet.entity.Transaction;
import com.nexuspay.wallet.entity.TransactionType;
import com.nexuspay.wallet.entity.User;
import com.nexuspay.wallet.exception.InsufficientFundsException;
import com.nexuspay.wallet.exception.UserNotFoundException;
import com.nexuspay.wallet.repository.AccountRepository;
import com.nexuspay.wallet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    
    public void createAccountForUser(User user) {
        Account account = new Account();
        // UUID garantiza que no haya colisiones en el número de cuenta
        account.setAccountNumber("ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        account.setBalance(BigDecimal.ZERO);
        account.setReservedBalance(BigDecimal.ZERO);
        account.setUser(user);
        user.setAccount(account);
        accountRepository.save(account);
    }

   
    public Account getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    
    public BigDecimal getAvailableBalance(Long userId) {
        Account account = getAccountByUserId(userId);
        return account.getBalance().subtract(account.getReservedBalance());
    }

    
    @Transactional
    public void transfer(Long senderUserId, String receiverAccountNumber, BigDecimal amount) {
        // Obtenemos las cuentas de origen y destino
        Account senderAccount = getAccountByUserId(senderUserId);
        Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada: " + receiverAccountNumber));

        // Validamos que el saldo disponible sea suficiente
        BigDecimal availableBalance = senderAccount.getBalance()
                .subtract(senderAccount.getReservedBalance());

        if (availableBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        // Movemos el dinero
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // Registramos la transacción en ambas cuentas para el historial
        saveTransaction(senderAccount, amount, TransactionType.DEBIT,
                "Transferencia enviada a " + receiverAccountNumber);
        saveTransaction(receiverAccount, amount, TransactionType.CREDIT,
                "Transferencia recibida de " + senderAccount.getAccountNumber());
    }

   
    public List<Transaction> getTransactionHistory(Long userId) {
        Account account = getAccountByUserId(userId);
        return transactionRepository.findByAccountIdOrderByTimestampDesc(account.getId());
    }

    
    private void saveTransaction(Account account, BigDecimal amount,
                                  TransactionType type, String description) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transactionRepository.save(transaction);
    }
}