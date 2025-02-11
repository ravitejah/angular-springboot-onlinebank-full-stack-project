package com.onlinebank.entity;
 
import jakarta.persistence.*;
import java.time.LocalDateTime;

//import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
 
@Entity
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the transaction", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
 
    private Double amount;
 
    private String transactionType;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
 
    private LocalDateTime transactionDateTime; // Add this field
 
    // Getters and Setters
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
this.id = id;
    }
 
    public Double getAmount() {
        return amount;
    }
 
    public void setAmount(Double amount) {
        this.amount = amount;
    }
 
    public String getTransactionType() {
        return transactionType;
    }
 
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
 
    public Account getAccount() {
        return account;
    }
 
    public void setAccount(Account account) {
        this.account = account;
    }
 
    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }
 
    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }
}