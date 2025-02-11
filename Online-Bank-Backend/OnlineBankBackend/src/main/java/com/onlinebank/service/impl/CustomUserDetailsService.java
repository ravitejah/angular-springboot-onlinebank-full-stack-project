package com.onlinebank.service.impl;

import com.onlinebank.entity.Admin;
import com.onlinebank.entity.BankUser;
import com.onlinebank.repository.AdminRepository;
import com.onlinebank.repository.BankUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private BankUserRepository bankUserRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if user exists as a BankUser
        BankUser bankUser = bankUserRepository.findByEmail(username).orElse(null);
        if (bankUser != null) {
            return User.builder()
                    .username(bankUser.getEmail())
                    .password(bankUser.getPassword())
                    .roles("USER") // Assign USER role
                    .build();
        }

        // Check if user exists as an Admin
        Admin admin = adminRepository.findByEmail(username).orElse(null);
        if (admin != null) {
            return User.builder()
                    .username(admin.getEmail())
                    .password(admin.getPassword())
                    .roles("ADMIN") // Assign ADMIN role
                    .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + username);
    }

    // Get role by email
    public String getRoleByEmail(String email) {
        if (bankUserRepository.findByEmail(email).isPresent()) {
            return "USER";
        } else if (adminRepository.findByEmail(email).isPresent()) {
            return "ADMIN";
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
