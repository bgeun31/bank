package com.example.bankapp.controller;

import com.example.bankapp.entity.Account;
import com.example.bankapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.bankapp.entity.Transaction;

import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/create")
    public String create(@RequestParam String ownerName, Model model) {
        Account account = accountService.createAccount(ownerName);
        model.addAttribute("account", account);
        return "result";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam double amount, Model model) {
        Account account = accountService.deposit(accountNumber, amount);
        model.addAttribute("account", account);
        return "result";
    }
    
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam double amount, Model model) {
        Account account = accountService.withdraw(accountNumber, amount);
        model.addAttribute("account", account);
        return "result";
    }
    
    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccountNumber,
                           @RequestParam String toAccountNumber,
                           @RequestParam double amount,
                           Model model) {
        Account result = accountService.transfer(fromAccountNumber, toAccountNumber, amount);
        model.addAttribute("account", result);
        return "result";
    }
    
    @GetMapping("/transactions")
    public String viewTransactions(@RequestParam String accountNumber, Model model) {
        List<Transaction> transactions = accountService.getTransactions(accountNumber);
        model.addAttribute("transactions", transactions);
        model.addAttribute("accountNumber", accountNumber);
        return "transactions";
    }
}
