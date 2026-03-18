package com.nexuspay.wallet.service;

import com.nexuspay.wallet.entity.*;
import com.nexuspay.wallet.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class WalletService {
    private final AccountRepository accountRepo;
    private final UserRepository userRepo;

    public WalletService(AccountRepository accountRepo, UserRepository userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void registerUser(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(name);

        Account acc = new Account();
        acc.setAccountNumber("ACC-" + System.currentTimeMillis());
        acc.setUser(user);
        user.setAccount(acc);

        userRepo.save(user);
    }
}