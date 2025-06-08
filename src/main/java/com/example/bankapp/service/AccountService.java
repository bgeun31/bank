package com.example.bankapp.service;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private String generateAccountNumber() {
        return String.format("%03d-%03d-%03d",
                (int) (Math.random() * 1000),
                (int) (Math.random() * 1000),
                (int) (Math.random() * 1000)
        );
    }

    public Account createAccount(String ownerName) {
        Account acc = new Account();
        acc.setOwnerName(ownerName);

        acc.setAccountNumber(generateAccountNumber());
        acc.setBalance(0);
        return accountRepository.save(acc);
    }

    public Account deposit(String accountNumber, double amount, String description) {
        Account acc = accountRepository.findByAccountNumber(accountNumber).orElseThrow();

        if (!acc.isActive()) {
            throw new IllegalStateException("비활성 계좌에는 입금이 불가능합니다.");
        }

        acc.setBalance(acc.getBalance() + amount);
        accountRepository.save(acc);

        String desc = (description != null && !description.isBlank()) ? description : "입금 처리";
        recordTransaction(accountNumber, "DEPOSIT", amount, desc);
        return acc;
    }

    public Account withdraw(String accountNumber, double amount, String description) {
        Account acc = accountRepository.findByAccountNumber(accountNumber).orElseThrow();

        if (!acc.isActive()) {
            throw new IllegalStateException("비활성 계좌에서는 출금이 불가능합니다.");
        }

        if (acc.getBalance() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        acc.setBalance(acc.getBalance() - amount);
        accountRepository.save(acc);

        String desc = (description != null && !description.isBlank()) ? description : "출금 처리";
        recordTransaction(accountNumber, "WITHDRAW", amount, desc);
        return acc;
    }

    public Account transfer(String fromAccount, String toAccount, double amount, String description) {
        Account from = accountRepository.findByAccountNumber(fromAccount).orElseThrow();
        Account to = accountRepository.findByAccountNumber(toAccount).orElseThrow();

        if (!from.isActive() || !to.isActive()) {
            throw new IllegalStateException("비활성 계좌와는 이체가 불가능합니다.");
        }

        if (from.getBalance() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);

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
        return accountRepository.findByUsername(username);
    }

    public Account createAccount(String username, String name, String type, double amount) {
        Account acc = new Account();
        acc.setUsername(username);
        acc.setOwnerName(name);
        acc.setAccountName(name);
        acc.setAccountType(type);
        acc.setAccountNumber(generateAccountNumber());
        acc.setBalance(amount);
        return accountRepository.save(acc);
    }

    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteAccount(String accountNumber, String username) {
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        if (!acc.getUsername().equals(username)) {
            throw new SecurityException("계좌 소유자만 삭제할 수 있습니다.");
        }

        transactionRepository.deleteByAccountNumber(accountNumber);
        accountRepository.delete(acc);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public void toggleAccountStatus(Long id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));
        acc.setActive(!acc.isActive());
        accountRepository.save(acc);
    }
}
