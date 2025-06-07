package com.example.bankapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerName;
    private String accountNumber;
    private double balance;
    private String accountName; // 주계좌, 적금계좌 등
    private LocalDateTime createdAt;
    private String type;         // "checking" or "saving"
    private String username; // 사용자 아이디

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    // Getters and Setters
    public Long getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
