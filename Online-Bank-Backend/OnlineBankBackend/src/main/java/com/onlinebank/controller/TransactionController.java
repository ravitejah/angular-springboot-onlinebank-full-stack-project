package com.onlinebank.controller;

import com.onlinebank.dto.AccountDTO;
import com.onlinebank.dto.TransactionDTO;
import com.onlinebank.entity.Transaction;
import com.onlinebank.exception.UnauthorizedException;
import com.onlinebank.service.AccountService;
import com.onlinebank.service.TransactionService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    // Perform a transaction
    @PostMapping("/{accountId}")
    public TransactionDTO performTransaction(@PathVariable Long accountId, @RequestBody TransactionDTO transactionDTO, Authentication authentication) {
        // Validate ownership of the account before proceeding
        validateAccountOwnership(accountId, authentication);
        Transaction transaction = mapToEntity(transactionDTO);
        Transaction savedTransaction = transactionService.performTransaction(accountId, transaction);
        return mapToDTO(savedTransaction);
    }
    
    private Transaction mapToEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setId(dto.getTransactionId());
        transaction.setTransactionDateTime(dto.getTransactionDateTime());
       
        return transaction;
    }

    // Get a transaction by ID
    @GetMapping("/{transactionId}")
    public TransactionDTO getTransactionById(@PathVariable Long transactionId, Authentication authentication) {
        TransactionDTO transactionDTO = transactionService.getTransactionById(transactionId);
        validateAccountOwnership(transactionDTO.getAccountId(), authentication);
        return transactionDTO;
    }


    // Get all transactions of the authenticated user
    @GetMapping
    public List<TransactionDTO> getAllTransactions(Authentication authentication) {
        String currentUserEmail = authentication.getName();
        
        // ✅ First, get all accounts of the logged-in user
        List<AccountDTO> userAccounts = accountService.getAllAccounts().stream()
                .filter(account -> account.getEmail().equals(currentUserEmail))
                .collect(Collectors.toList());
 
        // 🚨 If the user has no accounts, return an empty list
        if (userAccounts.isEmpty()) {
            System.out.println("🚨 User has no accounts, returning empty transactions.");
            return new ArrayList<>();
        }
 
        // ✅ Filter transactions only for the user's accounts
        return transactionService.getAllTransactions().stream()
                .filter(transaction -> userAccounts.stream()
                    .anyMatch(account -> account.getAccountNumber().equals(transaction.getAccountNumber()))
                )
                .collect(Collectors.toList());
    }

    // Validate that the authenticated user owns the account associated with the transaction
    private void validateAccountOwnership(Long accountId, Authentication authentication) {
        String currentUsername = authentication.getName();
        // Check if the account belongs to the current user
        AccountDTO accountDTO = accountService.getAccountById(accountId);
        if (!accountDTO.getEmail().equals(currentUsername)) {
            throw new UnauthorizedException("You do not have access to this account.");
        }
    }

    // Map Transaction entity to TransactionDTO
    private TransactionDTO mapToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(transaction.getId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setAccountNumber(transaction.getAccount().getAccountNumber());
        dto.setAccountId(transaction.getAccount().getId());
        dto.setTransactionDateTime(transaction.getTransactionDateTime());
        return dto;
    }

}
