package com.example.personalfinanceapp.service;

import com.example.personalfinanceapp.model.Transaction;
import com.example.personalfinanceapp.model.User;
import com.example.personalfinanceapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    public List<Transaction> getAllTransactionsByUser(User user) {
        return transactionRepository.findByUserOrderByDateDesc(user);
    }
    
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
    
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    
    public Transaction updateTransaction(Long id, Transaction transactionDetails, User user) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isPresent()) {
            Transaction transaction = existingTransaction.get();
            
            // Check if user owns this transaction or is admin
            if (!transaction.getUser().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
                throw new RuntimeException("Access denied");
            }
            
            transaction.setAmount(transactionDetails.getAmount());
            transaction.setType(transactionDetails.getType());
            transaction.setDate(transactionDetails.getDate());
            transaction.setDescription(transactionDetails.getDescription());
            
            return transactionRepository.save(transaction);
        }
        throw new RuntimeException("Transaction not found");
    }
    
    public void deleteTransaction(Long id, User user) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            // Check if user owns this transaction or is admin
            if (!transaction.get().getUser().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
                throw new RuntimeException("Access denied");
            }
            transactionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Transaction not found");
        }
    }
} 