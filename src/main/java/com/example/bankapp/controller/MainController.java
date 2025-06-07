package com.example.bankapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.List;
import java.util.Comparator;

import com.example.bankapp.entity.Account;
import com.example.bankapp.entity.Transaction;
import com.example.bankapp.service.AccountService;  // 필요시

@Controller
public class MainController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);

        List<Account> accounts = accountService.getAccounts(username); // 사용자 계좌
        List<Transaction> transactions = accounts.stream()
            .flatMap(acc -> accountService.getTransactions(acc.getAccountNumber()).stream())
            .sorted(Comparator.comparing(Transaction::getDate).reversed())
            .limit(3)
            .toList();

        double total = accounts.stream().mapToDouble(Account::getBalance).sum();

        model.addAttribute("accounts", accounts);
        model.addAttribute("totalBalance", total);
        model.addAttribute("accountCount", accounts.size());
        model.addAttribute("recentTransactions", transactions);
        return "dashboard";
    }
}

