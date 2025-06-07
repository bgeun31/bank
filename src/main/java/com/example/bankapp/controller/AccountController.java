package com.example.bankapp.controller;

import com.example.bankapp.entity.Account;
import com.example.bankapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.bankapp.entity.Transaction;
import java.security.Principal;


import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String home() {
        return "index";
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
    
    @GetMapping("/accounts")
    public String accounts(Model model, Principal principal) {
        String username = principal.getName();
        List<Account> accounts = accountService.getAccountsByUsername(username);
        model.addAttribute("accounts", accounts);
        return "account"; // account.html 렌더링
    }

    @GetMapping("/create")
    public String showCreateForm() {
        return "create-account"; // 위 템플릿 렌더링
    }

    @PostMapping("/create")
    public String handleCreate(
        @RequestParam String accountName,
        @RequestParam String accountType,
        @RequestParam double initialBalance,
        Principal principal
    ) {
        String username = principal.getName();
        accountService.createAccount(username, accountName, accountType, initialBalance);
        return "redirect:/accounts";
    }
    @GetMapping("/account/detail")
    public String accountDetail(@RequestParam String accountNumber, Model model) {
        Account account = accountService.findByAccountNumber(accountNumber);
        List<Transaction> transactions = accountService.getTransactions(accountNumber);
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        return "account-detail";
    }
}
