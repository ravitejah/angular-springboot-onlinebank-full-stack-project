package com.onlinebank.controller;

import com.onlinebank.dto.AccountDTO;
import com.onlinebank.entity.Account;
import com.onlinebank.entity.BankUser;
import com.onlinebank.exception.UnauthorizedException;
import com.onlinebank.repository.BankUserRepository;
import com.onlinebank.service.AccountService;
import com.onlinebank.service.BankUserService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BankUserService bankUserService;
    
    @Autowired
    private BankUserRepository userRepo;

    @PostMapping("/{userId}")
    public AccountDTO createAccount(@RequestBody Account account, @PathVariable Long userId, Authentication authentication) {
//    	AccountDTO accountDTO = accountService.getAccountById(id);
//        validateAccountOwnership(accountDTO.getEmail(), authentication);
//    	BankUser user=userRepo.getById(userId);
    	String email=authentication.getName();
    	BankUser user=bankUserService.loadUserByEmail(email);
    	if(!user.getId().equals(userId) && (!authentication.getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ROLE_ADMIN")) || !authentication.getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ADMIN")) ) ){
    		throw new UnauthorizedException("You do not have permission to create an account for this user.");
    	}
        Account createdAccount = accountService.createAccount(account, userId);
        return mapToDTO(createdAccount); // Convert the Account entity to AccountDTO
    }
    
//    @GetMapping("/balance/{accountNumber}")
//    public Double getAccountBalance(@PathVariable String accountNumber, Authentication authentication) {
//        return accountService.getAccountBalance(accountNumber, authentication.getName());
//    }
    
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<Double> getAccountBalance(
            @PathVariable String accountNumber,
            Authentication authentication) {

        System.out.println("Requested Account Number: " + accountNumber);
        System.out.println("Authenticated User: " + authentication.getName());

        Double balance = accountService.getAccountBalance(accountNumber, authentication.getName());

        if (balance == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(balance);
    }


    private AccountDTO mapToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setId(account.getId());
        dto.setBalance(account.getBalance());
//        System.out.println(account.getBankUser().getEmail());
        dto.setEmail(account.getBankUser().getEmail());
        dto.setBankUserName(account.getBankUser().getName());
        return dto;
    }

    @GetMapping("/{id}")
    public AccountDTO getAccountById(@PathVariable Long id, Authentication authentication) {
        AccountDTO accountDTO = accountService.getAccountById(id);
        validateAccountOwnership(accountDTO.getEmail(), authentication);
        return accountDTO;
    }

    @GetMapping("/by-account-number/{accountNumber}")
    public AccountDTO getAccountByAccountNumber(@PathVariable String accountNumber, Authentication authentication) {
        AccountDTO accountDTO = accountService.getAccountByAccountNumber(accountNumber);
        validateAccountOwnership(accountDTO.getEmail(), authentication);
        return accountDTO;
    }
    
    @GetMapping
    public List<AccountDTO> getAllAccounts(Authentication authentication) {
        String currentUserEmail = authentication.getName();
        return accountService.getAllAccounts().stream()
                .filter(account -> account.getEmail().equals(currentUserEmail)) // Filter by email
                .collect(Collectors.toList());
    }




    private void validateAccountOwnership(String bankUserName, Authentication authentication) {
        String currentUsername = authentication.getName();
        System.out.println(currentUsername+" : "+bankUserName);
        if (!bankUserName.equals(currentUsername)) {
            throw new UnauthorizedException("You do not have access to this account.");
        }
    }
}
