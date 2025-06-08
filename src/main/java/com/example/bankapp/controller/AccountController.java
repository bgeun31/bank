package com.example.bankapp.controller;

import com.example.bankapp.entity.Account;
import com.example.bankapp.service.TransactionService;

import java.util.ArrayList;

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
	private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam double amount, Model model) {
        Account account = accountService.withdraw(accountNumber, amount);
        model.addAttribute("account", account);
        return "result";
    }
    
    @GetMapping("/accounts")
    public String accounts(Model model, Principal principal) {
        String username = principal.getName();
        List<Account> accounts = accountService.getAccountsByUsername(username);
        model.addAttribute("accounts", accounts);
        return "account"; // account.html 렌더링
    }

    @GetMapping("/create-account")
    public String showCreateAccountPage() {
        return "create-account"; // create-account.html 템플릿 렌더링
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
        model.addAttribute("account", account);         // 계좌 상세 정보
        model.addAttribute("transactions", transactions); // 거래 내역
        return "account-detail";
    }
    @GetMapping("/transfer")
    public String showTransferForm(Model model, Principal principal) {
        String username = principal.getName();
        List<Account> accounts = accountService.getAccountsByUsername(username);
        model.addAttribute("accounts", accounts);
        return "transfer"; // transfer.html 템플릿 렌더링
    }
    @PostMapping("/transfer")
    public String handleTransfer(
        @RequestParam String fromAccountNumber,
        @RequestParam String toAccountNumber,
        @RequestParam double amount,
        @RequestParam(required = false) String description,
        Model model
    ) {
        Account updatedAccount = accountService.transfer(fromAccountNumber, toAccountNumber, amount, description);
        model.addAttribute("account", updatedAccount); // 이체 후 내 계좌 상태
        return "transfer-result"; // 이 템플릿 렌더링
    }
    @GetMapping("/transactions")
    public String viewUserTransactions(Model model, Principal principal) {
        String username = principal.getName();
        List<Account> accounts = accountService.getAccountsByUsername(username);

        List<Transaction> allTransactions = new ArrayList<>();
        for (Account acc : accounts) {
            allTransactions.addAll(accountService.getTransactions(acc.getAccountNumber()));
        }

        model.addAttribute("transactions", allTransactions); // ✅ 필수
        return "transactions"; // -> templates/transactions.html
    }

    // 특정 계좌 거래 조회 (URI 다르게 지정)
    @GetMapping("/transactions/account")
    public String viewAccountTransactions(@RequestParam String accountNumber, Model model) {
        List<Transaction> transactions = accountService.getTransactions(accountNumber);
        model.addAttribute("transactions", transactions);
        return "transactions";
    }
    @PostMapping("/deposit")
    public String handleDeposit(@RequestParam String accountNumber,
                                @RequestParam double amount,
                                @RequestParam(required = false) String description) {
        transactionService.deposit(accountNumber, amount,
            (description != null && !description.isBlank()) ? description : "입금");
        return "redirect:/transactions";
    }

    @GetMapping("/deposit")
    public String showDepositForm(Model model, Principal principal) {
        String username = principal.getName();
        List<Account> accounts = accountService.getAccountsByUsername(username);
        model.addAttribute("accounts", accounts);
        return "deposit";
    }
}
