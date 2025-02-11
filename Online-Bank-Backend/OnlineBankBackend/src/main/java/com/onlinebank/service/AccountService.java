package com.onlinebank.service;

import com.onlinebank.dto.AccountDTO;
import com.onlinebank.entity.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account, Long userId);
    AccountDTO getAccountById(Long id);
    AccountDTO getAccountByAccountNumber(String accountNumber); // Add this method
    AccountDTO updateAccount(Long id, Account account);
    void deleteAccount(Long id);
    List<AccountDTO> getAllAccounts();
    Double getAccountBalance(String accountNumber, String userEmail);
}

