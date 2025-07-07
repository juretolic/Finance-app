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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("")
    public String listTransactions(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        
        User user = userOpt.get();
        List<Transaction> transactions = transactionService.getAllTransactionsByUser(user);
        
        model.addAttribute("transactions", transactions);
        model.addAttribute("user", user);
        
        return "transactions";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transaction_form";
    }
    
    @PostMapping("/add")
    public String addTransaction(@RequestParam BigDecimal amount,
                                @RequestParam Transaction.TransactionType type,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                @RequestParam String description,
                                RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        
        try {
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setType(type);
            transaction.setDate(date);
            transaction.setDescription(description);
            transaction.setUser(userOpt.get());
            
            transactionService.saveTransaction(transaction);
            redirectAttributes.addFlashAttribute("successMessage", "Transaction added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding transaction: " + e.getMessage());
        }
        
        return "redirect:/transactions";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        
        Optional<Transaction> transactionOpt = transactionService.getTransactionById(id);
        if (transactionOpt.isEmpty()) {
            return "redirect:/transactions";
        }
        
        Transaction transaction = transactionOpt.get();
        User user = userOpt.get();
        
        // Check if user owns this transaction or is admin
        if (!transaction.getUser().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            return "redirect:/transactions";
        }
        
        model.addAttribute("transaction", transaction);
        return "transaction_form";
    }
    
    @PostMapping("/edit/{id}")
    public String updateTransaction(@PathVariable Long id,
                                   @RequestParam BigDecimal amount,
                                   @RequestParam Transaction.TransactionType type,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam String description,
                                   RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        
        try {
            Transaction transactionDetails = new Transaction();
            transactionDetails.setAmount(amount);
            transactionDetails.setType(type);
            transactionDetails.setDate(date);
            transactionDetails.setDescription(description);
            
            transactionService.updateTransaction(id, transactionDetails, userOpt.get());
            redirectAttributes.addFlashAttribute("successMessage", "Transaction updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating transaction: " + e.getMessage());
        }
        
        return "redirect:/transactions";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }
        
        try {
            transactionService.deleteTransaction(id, userOpt.get());
            redirectAttributes.addFlashAttribute("successMessage", "Transaction deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting transaction: " + e.getMessage());
        }
        
        return "redirect:/transactions";
    }
} 