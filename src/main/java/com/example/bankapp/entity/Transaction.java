package com.example.bankapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 기존 필드
    private String accountNumber;
    private String type; // "DEPOSIT", "WITHDRAW", "TRANSFER"
    private double amount;
    private LocalDateTime date;
    private String description;

    // 🔽 추가: 실제 계좌 객체와 연결
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // 🔽 Getter / Setter 추가
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    // 나머지 Getter/Setter 생략...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
