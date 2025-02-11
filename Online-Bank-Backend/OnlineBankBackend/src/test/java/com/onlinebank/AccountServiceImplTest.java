package com.onlinebank;

import com.onlinebank.dto.AccountDTO;
import com.onlinebank.entity.Account;
import com.onlinebank.entity.BankUser;
import com.onlinebank.exception.ResourceNotFoundException;
import com.onlinebank.exception.UnauthorizedException;
import com.onlinebank.repository.AccountRepository;
import com.onlinebank.repository.BankUserRepository;
import com.onlinebank.service.impl.AccountServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
class AccountServiceImplTest {
 
    @InjectMocks
    private AccountServiceImpl accountService;
 
    @Mock
    private AccountRepository accountRepository;
 
    @Mock
    private BankUserRepository userRepository;
 
    private BankUser user;
    private Account account;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
 
        // Mock BankUser
        user = new BankUser();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
 
        // Mock Account
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("CTS000001");
        account.setBalance(1000.0);
        account.setBankUser(user);
    }
 
    @Test
    void createAccount_WhenUserExists_ShouldCreateAccount() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(accountRepository.count()).thenReturn(0L);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
 
        // Act
        Account result = accountService.createAccount(account, user.getId());
 
        // Assert
        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.getAccountNumber());
        verify(userRepository, times(1)).findById(user.getId());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
 
    @Test
    void createAccount_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
 
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(account, user.getId()));
        verify(userRepository, times(1)).findById(user.getId());
    }
 
    @Test
    void getAccountBalance_WhenAccountExistsAndUserAuthorized_ShouldReturnBalance() {
        // Arrange
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
 
        // Act
        Double balance = accountService.getAccountBalance(account.getAccountNumber(), user.getEmail());
 
        // Assert
        assertNotNull(balance);
        assertEquals(account.getBalance(), balance);
        verify(accountRepository, times(1)).findByAccountNumber(account.getAccountNumber());
    }
 
    @Test
    void getAccountBalance_WhenUserNotAuthorized_ShouldThrowException() {
        // Arrange
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
 
        // Act & Assert
        assertThrows(UnauthorizedException.class,
                () -> accountService.getAccountBalance(account.getAccountNumber(), "wrong@example.com"));
    }
 
    @Test
    void getAccountById_WhenAccountExists_ShouldReturnAccountDTO() {
        // Arrange
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
 
        // Act
        AccountDTO result = accountService.getAccountById(account.getId());
 
        // Assert
        assertNotNull(result);
        assertEquals(account.getAccountNumber(), result.getAccountNumber());
        verify(accountRepository, times(1)).findById(account.getId());
    }
 
    @Test
    void getAccountById_WhenAccountDoesNotExist_ShouldThrowException() {
        // Arrange
        when(accountRepository.findById(account.getId())).thenReturn(Optional.empty());
 
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(account.getId()));
    }
 

 
    @Test
    void deleteAccount_WhenAccountExists_ShouldDeleteAccount() {
        // Arrange
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
 
        // Act
        accountService.deleteAccount(account.getId());
 
        // Assert
        verify(accountRepository, times(1)).findById(account.getId());
        verify(accountRepository, times(1)).delete(account);
    }
 
    @Test
    void getAllAccounts_ShouldReturnListOfAccounts() {
        // Arrange
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountRepository.findAll()).thenReturn(accounts);
 
        // Act
        List<AccountDTO> result = accountService.getAllAccounts();
 
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(accountRepository, times(1)).findAll();
    }
}
