package com.example.bankapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.service.AccountService;  // 필요시

@Controller
public class MainController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        String username = principal.getName();

        // 사용자 계좌 목록
        List<Account> accounts = accountService.getAccountsByUsername(username);
        model.addAttribute("accounts", accounts);

        // 총 자산 계산
        double totalBalance = accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
        model.addAttribute("totalBalance", totalBalance);   // 숫자로 전달

        // 계좌 수
        model.addAttribute("accountCount", accounts.size());

        // 최근 거래 내역
        List<Transaction> recentTransactions = new ArrayList<>();
        for (Account acc : accounts) {
            List<Transaction> tx = accountService.getTransactions(acc.getAccountNumber());
            recentTransactions.addAll(tx);
        }

        // 최근 거래 5개만 출력
        recentTransactions.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        List<Transaction> last5 = recentTransactions.stream().limit(5).toList();

        model.addAttribute("recentTransactionCount", recentTransactions.size());
        model.addAttribute("recentTransactions", last5);

        return "dashboard";
    }
}

