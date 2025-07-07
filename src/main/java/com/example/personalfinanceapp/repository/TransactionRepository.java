package com.example.personalfinanceapp.repository;

import com.example.personalfinanceapp.model.Transaction;
import com.example.personalfinanceapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByUserOrderByDateDesc(User user);
    
    List<Transaction> findByUserAndTypeOrderByDateDesc(User user, Transaction.TransactionType type);
} 