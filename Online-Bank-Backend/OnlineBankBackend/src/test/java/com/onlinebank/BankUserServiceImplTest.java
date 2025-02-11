package com.onlinebank;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.onlinebank.entity.BankUser;
import com.onlinebank.exception.ResourceNotFoundException;
import com.onlinebank.repository.BankUserRepository;
import com.onlinebank.service.impl.BankUserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import java.util.Optional;
 
//import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
class BankUserServiceImplTest {
 
    @InjectMocks
    private BankUserServiceImpl bankUserService;
 
    @Mock
    private BankUserRepository userRepository;
 
    @Mock
    private PasswordEncoder passwordEncoder;
 
    private BankUser user;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
 
        // Create a mock user
        user = new BankUser();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setMobileNumber("9876543210");
    }
 
    @Test
    void registerUser_WhenEmailDoesNotExist_ShouldRegisterUser() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(BankUser.class))).thenReturn(user);
 
        // Act
        BankUser result = bankUserService.registerUser(user);
 
        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(BankUser.class));
    }
 
    @Test
    void registerUser_WhenEmailExists_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
 
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bankUserService.registerUser(user));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }
 
    @Test
    void loadUserByEmail_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
 
        // Act
        BankUser result = bankUserService.loadUserByEmail(user.getEmail());
 
        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }
 
    @Test
    void loadUserByEmail_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
 
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bankUserService.loadUserByEmail(user.getEmail()));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }
 
    @Test
    void updateBankUser_WhenUserExists_ShouldUpdateUser() {
        // Arrange
        BankUser updatedUser = new BankUser();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane@example.com");
        updatedUser.setMobileNumber("9876543211");
 
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(BankUser.class))).thenReturn(updatedUser);
 
        // Act
        BankUser result = bankUserService.updateBankUser(user.getId(), updatedUser);
 
        // Assert
        assertNotNull(result);
        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(any(BankUser.class));
    }
 
    @Test
    void updateBankUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
 
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bankUserService.updateBankUser(user.getId(), user));
        verify(userRepository, times(1)).findById(user.getId());
    }
 
    @Test
    void deleteBankUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
 
        // Act
        bankUserService.deleteBankUser(user.getId());
 
        // Assert
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).delete(user);
    }
 
    @Test
    void deleteBankUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
 
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bankUserService.deleteBankUser(user.getId()));
        verify(userRepository, times(1)).findById(user.getId());
    }
}