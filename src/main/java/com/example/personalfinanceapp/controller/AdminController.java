package com.example.personalfinanceapp.controller;

import com.example.personalfinanceapp.model.Transaction;
import com.example.personalfinanceapp.model.User;
import com.example.personalfinanceapp.service.TransactionService;
import com.example.personalfinanceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TransactionService transactionService;
    
    @GetMapping("/users")
    public String adminUsers(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty() || userOpt.get().getRole() != User.Role.ADMIN) {
            return "redirect:/transactions";
        }
        
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", userOpt.get());
        
        return "admin_users";
    }
    
    @GetMapping("/transactions")
    public String adminTransactions(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty() || userOpt.get().getRole() != User.Role.ADMIN) {
            return "redirect:/transactions";
        }
        
        List<Transaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        model.addAttribute("currentUser", userOpt.get());
        
        return "admin_transactions";
    }
} 