package com.example.bankapp.repository;

import com.example.bankapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumberOrderByDateDesc(String accountNumber);
    void deleteByAccountNumber(String accountNumber); // 🔥 추가
}
