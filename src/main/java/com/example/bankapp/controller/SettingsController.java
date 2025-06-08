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
public class SettingsController {

    @GetMapping("/settings")
    public String settingsPage(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "settings";
    }

    @PostMapping("/settings/update-password")
    public String updatePassword(@RequestParam String password, Principal principal) {
        // TODO: 실제로 비밀번호를 업데이트하는 로직 구현
        System.out.println("새 비밀번호: " + password + ", 사용자: " + principal.getName());
        return "redirect:/settings?success";
    }
}
