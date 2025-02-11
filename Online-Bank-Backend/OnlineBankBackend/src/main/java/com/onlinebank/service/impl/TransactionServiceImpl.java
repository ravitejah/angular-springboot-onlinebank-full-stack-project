package com.onlinebank.service.impl;

import com.onlinebank.dto.TransactionDTO;
import com.onlinebank.entity.Account;
import com.onlinebank.entity.Transaction;
import com.onlinebank.exception.ResourceNotFoundException;
import com.onlinebank.repository.AccountRepository;
import com.onlinebank.repository.TransactionRepository;
import com.onlinebank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    

    @Override
    public Transaction performTransaction(Long accountId, Transaction transaction) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));
        
        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than 0.");
        }
        
        // Ensure balance is initialized
        if (account.getBalance() == null) {
            account.setBalance(0.0);
        }
 
        // Process transaction
        if ("DEBIT".equalsIgnoreCase(transaction.getTransactionType())) {
            if (account.getBalance() < transaction.getAmount()) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            account.setBalance(account.getBalance() - transaction.getAmount());
        } else if ("CREDIT".equalsIgnoreCase(transaction.getTransactionType())) {
            account.setBalance(account.getBalance() + transaction.getAmount());
        } else {
            throw new IllegalArgumentException("Invalid transaction type. Use 'CREDIT' or 'DEBIT'.");
        }
 
        // Set account and transactionDateTime
        transaction.setAccount(account);
        transaction.setTransactionDateTime(LocalDateTime.now());
 
        // Save transaction and update account
        Transaction savedTransaction = transactionRepository.save(transaction);
        accountRepository.save(account);
 
        return savedTransaction;
    }




    @Override
    public TransactionDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + transactionId));
        return mapToDTO(transaction);
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
    	
    	List<Transaction> transactions=transactionRepository.findAll();
    	transactions.forEach(transaction ->{
    		System.out.println("T id :"+ transaction.getId());
    		System.out.println("account Id :" + transaction.getAccount().getId());
    	});
    	
        return transactionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

//    private TransactionDTO mapToDTO(Transaction transaction) {
//        TransactionDTO dto = new TransactionDTO();
//        dto.setTransactionId(transaction.getId());
//        dto.setTransactionType(transaction.getTransactionType());
//        dto.setAmount(transaction.getAmount());
//        dto.setAccountNumber(transaction.getAccount().getAccountNumber());
//        dto.setTransactionDateTime(transaction.getTransactionDate()); 
//        dto.setAccounId(transaction.getAccount().getId());
//        return dto;
//    }
    
    private TransactionDTO mapToDTO(Transaction transaction) {
        if (transaction.getAccount() == null || transaction.getAccount().getId() == null) {
            throw new IllegalArgumentException("Transaction doesn't have a valid account ID");
        }
     
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(transaction.getId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setAccountNumber(transaction.getAccount().getAccountNumber());
        dto.setAccountId(transaction.getAccount().getId());
        dto.setTransactionDateTime(transaction.getTransactionDateTime()); // Map transactionDateTime
        return dto;
    }

}
