package com.onlinebank.dto;
 
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {
	
    @Schema(description = "The unique identifier of the transaction", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long transactionId;
    private String transactionType;
    private double amount;
    
    @Schema( accessMode = Schema.AccessMode.READ_ONLY)
    private String accountNumber;
    
    @Schema( accessMode = Schema.AccessMode.READ_ONLY)
    private Long accountId; // Correct spelling
    
    @Schema( accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime transactionDateTime; // Ensure this is mapped properly
 
    // Getters and Setters
    public Long getTransactionId() {
        return transactionId;
    }
 
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
 
    public String getTransactionType() {
        return transactionType;
    }
 
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
 
    public double getAmount() {
        return amount;
    }
 
    public void setAmount(double amount) {
        this.amount = amount;
    }
 
    public String getAccountNumber() {
        return accountNumber;
    }
 
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
 
    public Long getAccountId() {
        return accountId;
    }
 
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
 
    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }
 
    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }
}