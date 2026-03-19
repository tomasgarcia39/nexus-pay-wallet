package com.nexuspay.wallet.repository;

import com.nexuspay.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Trae todas las transacciones de una cuenta, ordenadas de más nueva a más vieja
    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);
}