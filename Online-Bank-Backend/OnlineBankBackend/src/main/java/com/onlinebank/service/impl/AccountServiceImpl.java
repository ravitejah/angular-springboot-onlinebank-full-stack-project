package com.onlinebank.service.impl;

import com.onlinebank.dto.AccountDTO;
import com.onlinebank.entity.Account;
import com.onlinebank.entity.BankUser;
import com.onlinebank.exception.ResourceNotFoundException;
import com.onlinebank.exception.UnauthorizedException;
import com.onlinebank.repository.AccountRepository;
import com.onlinebank.repository.BankUserRepository;
import com.onlinebank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BankUserRepository userRepository;
    
//    private static final String ACCOUNT_PREFIX = "CTS";
//
//    @Override
//    public Account createAccount(Account account, Long userId) {
//        BankUser user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
//        
//        String accountNumber = generateAccountNumber();
//        account.setAccountNumber(accountNumber);
//        
//        account.setBankUser(user);
//        return accountRepository.save(account); // Return the Account entity
//    }
//    
//    private String generateAccountNumber() {
//        long count = accountRepository.count() + 1; // Add 1 to ensure sequential order
//        return String.format(ACCOUNT_PREFIX + "%06d", count); // Pad with leading zeros
//    }
    
    @Override
    public Account createAccount(Account account, Long userId) {
        BankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        account.setBankUser(user);

        // Step 1: Save the account first (to generate an ID)
        Account savedAccount = accountRepository.save(account);

        // Step 2: Generate the account number using the ID
        String accountNumber = String.format("CTS%06d", savedAccount.getId());

        // Step 3: Update the account with the generated number
        savedAccount.setAccountNumber(accountNumber);
        return accountRepository.save(savedAccount);
    }

    
    @Override
    public Double getAccountBalance(String accountNumber, String userEmail) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with account number: " + accountNumber));
     
        if (!account.getBankUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You do not have access to this account.");
        }
     
        return account.getBalance();
    }

    @Override
    public AccountDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
        return mapToDTO(account);
    }

    @Override
    public AccountDTO updateAccount(Long id, Account account) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
        existingAccount.setBalance(account.getBalance());
        return mapToDTO(accountRepository.save(existingAccount));
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
        accountRepository.delete(account);
    }
    
    @Override
    public AccountDTO getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with account number: " + accountNumber));
        return mapToDTO(account);
    }


    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AccountDTO mapToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setId(account.getId());
        dto.setBalance(account.getBalance());
        dto.setBankUserName(account.getBankUser().getName());
        dto.setEmail(account.getBankUser().getEmail());
        return dto;
    }
}

