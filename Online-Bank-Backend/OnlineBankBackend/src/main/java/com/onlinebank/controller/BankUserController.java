package com.onlinebank.controller;

import com.onlinebank.dto.BankUserDTO;
import com.onlinebank.dto.JwtRequestDTO;
import com.onlinebank.dto.JwtResponseDTO;
import com.onlinebank.entity.BankUser;
import com.onlinebank.exception.ResourceNotFoundException;
import com.onlinebank.repository.BankUserRepository;
import com.onlinebank.service.BankUserService;
import com.onlinebank.service.impl.CustomUserDetailsService;
import com.onlinebank.config.JwtUtils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class BankUserController {

    @Autowired
    private BankUserRepository bankUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private BankUserService bankUserService;

    // User Registration
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody BankUser bankUser) {
        // Check if the email is already in use
        if (bankUserRepository.findByEmail(bankUser.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN) // Force plain text
                    .body("Email already in use!");
        }
 
        // Encrypt the password before saving
        bankUser.setPassword(passwordEncoder.encode(bankUser.getPassword()));
        bankUserRepository.save(bankUser);
 
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN) // Force plain text
                .body("User registered successfully!");
    }

    // User Login
    @PostMapping("/login")
    public JwtResponseDTO login(@RequestBody JwtRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
//        String token = jwtUtils.generateToken(userDetails);
        String role = customUserDetailsService.getRoleByEmail(request.getEmail());
        
        String token = jwtUtils.generateToken(userDetails, role);

        return new JwtResponseDTO(token);
    }
    
    @GetMapping("/me")
    public BankUserDTO getMyDetails(Authentication authentication) {
        BankUser user = bankUserService.loadUserByEmail(authentication.getName());
        return mapToDTO(user);
    }
     
    private BankUserDTO mapToDTO(BankUser user) {
        BankUserDTO dto = new BankUserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        return dto;
    }

    // Forgot Password
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, @RequestParam String newPassword) {
        BankUser user = bankUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        user.setPassword(passwordEncoder.encode(newPassword));
        bankUserRepository.save(user);
        return "Password updated successfully!";
    }

    // Update Password
//    @PutMapping("/update-password")
//    public String updatePassword(Authentication authentication, @RequestParam String oldPassword, @RequestParam String newPassword) {
//        BankUser user = bankUserRepository.findByEmail(authentication.getName())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
//            throw new IllegalArgumentException("Old password is incorrect!");
//        }
//        user.setPassword(passwordEncoder.encode(newPassword));
//        bankUserRepository.save(user);
//        return "Password updated successfully!";
//    }
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(Authentication authentication, @RequestBody Map<String, String> requestBody) {
        System.out.println("Received Request Body: " + requestBody); // Debugging Log

        String oldPassword = requestBody.get("oldPassword");
        String newPassword = requestBody.get("newPassword");

        BankUser user = bankUserRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        bankUserRepository.save(user);

        return ResponseEntity.ok("Password updated successfully!");
    }



}

