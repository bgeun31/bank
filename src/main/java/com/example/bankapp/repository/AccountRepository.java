package com.example.bankapp.repository;

import com.example.bankapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    // 추가: 사용자 이름으로 계좌 목록 조회
    List<Account> findByOwnerName(String ownerName);
}
