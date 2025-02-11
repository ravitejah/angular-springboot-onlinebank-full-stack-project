package com.onlinebank.service;

import com.onlinebank.entity.BankUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface BankUserService extends UserDetailsService {
    BankUser registerUser(BankUser user);
    BankUser loadUserByEmail(String email);
    BankUser updateBankUser(Long id, BankUser user);
    void deleteBankUser(Long id);
}

