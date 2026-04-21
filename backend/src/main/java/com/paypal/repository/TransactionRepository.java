package com.paypal.repository;

import com.paypal.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByOrderId(UUID orderId);
    Optional<Transaction> findByTransactionId(String transactionId);
    List<Transaction> findByOrderIdOrderByCreatedAtDesc(UUID orderId);
}
