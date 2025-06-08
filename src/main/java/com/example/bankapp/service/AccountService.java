package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    
    private String generateAccountNumber() {
        return String.format("%03d-%03d-%03d",
                (int)(Math.random() * 1000),
                (int)(Math.random() * 1000),
                (int)(Math.random() * 1000)
        );
    }

    public Account createAccount(String ownerName) {
        Account acc = new Account();
        acc.setOwnerName(ownerName);

        // 사람 친화적인 계좌번호 생성 (예: 123-456-789)
        String accountNumber = String.format("%03d-%03d-%03d",
            (int)(Math.random() * 1000),
            (int)(Math.random() * 1000),
            (int)(Math.random() * 1000)
        );
        acc.setAccountNumber(accountNumber);

        acc.setBalance(0);
        return accountRepository.save(acc);
    }

    public Account deposit(String accountNumber, double amount, String description) {
        Account acc = accountRepository.findByAccountNumber(accountNumber).orElseThrow();
        acc.setBalance(acc.getBalance() + amount);
        Account updated = accountRepository.save(acc);

        String desc = (description != null && !description.isBlank()) ? description : "입금 처리";
        recordTransaction(accountNumber, "DEPOSIT", amount, desc);
        return updated;
    }

    public Account withdraw(String accountNumber, double amount, String description) {
        Account acc = accountRepository.findByAccountNumber(accountNumber).orElseThrow();
        if (acc.getBalance() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        acc.setBalance(acc.getBalance() - amount);
        Account updated = accountRepository.save(acc);

        String desc = (description != null && !description.isBlank()) ? description : "출금 처리";
        recordTransaction(accountNumber, "WITHDRAW", amount, desc);
        return updated;
    }

    public Account transfer(String fromAccount, String toAccount, double amount, String description) {
        Account from = accountRepository.findByAccountNumber(fromAccount).orElseThrow();
        Account to = accountRepository.findByAccountNumber(toAccount).orElseThrow();

        if (from.getBalance() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);

        // ✅ 사용자 입력한 description이 있으면 활용
        String fromDesc = description != null && !description.isBlank() ? description : toAccount + "로 이체";
        String toDesc = description != null && !description.isBlank() ? description : fromAccount + "로부터 이체";

        recordTransaction(fromAccount, "TRANSFER", -amount, fromDesc);
        recordTransaction(toAccount, "TRANSFER", amount, toDesc);

        return from;
    }

    private void recordTransaction(String accountNumber, String type, double amount, String description) {
        Transaction tx = new Transaction();
        tx.setAccountNumber(accountNumber);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setDate(LocalDateTime.now());
        tx.setDescription(description);
        transactionRepository.save(tx);
    }
    
    public List<Transaction> getTransactions(String accountNumber) {
        return transactionRepository.findByAccountNumberOrderByDateDesc(accountNumber);
    }
    
    public List<Account> getAccounts(String ownerName) {
        return accountRepository.findByOwnerName(ownerName);
    }
    
    public List<Account> getAccountsByUsername(String username) {
        return accountRepository.findByUsername(username); // ✅ 정확한 필드
    }
    
    public Account createAccount(String username, String name, String type, double amount) {
        Account acc = new Account();
        acc.setUsername(username);
        acc.setOwnerName(name);
        acc.setAccountName(name); // ✅ 이 줄 반드시 필요
        acc.setAccountType(type);
        acc.setAccountNumber(generateAccountNumber());
        acc.setBalance(amount);
        return accountRepository.save(acc);
    }

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));
    }
}
