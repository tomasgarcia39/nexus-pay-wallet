package com.nexuspay.wallet.service;

import com.nexuspay.wallet.entity.Account;
import com.nexuspay.wallet.entity.User;
import com.nexuspay.wallet.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class WalletService {

    @Autowired
    private AccountRepository accountRepository;

    public void createAccountForUser(User user) {
        Account account = new Account();
        // Generamos un número de cuenta simple basado en el tiempo
        account.setAccountNumber("ACC-" + System.currentTimeMillis());
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        
        // Vinculamos la cuenta al usuario (lo que daba error)
        user.setAccount(account);
        
        accountRepository.save(account);
    }
}