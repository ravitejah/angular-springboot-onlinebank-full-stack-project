package com.onlinebank.service.impl;

import com.onlinebank.dto.LoanDTO;
import com.onlinebank.entity.BankUser;
import com.onlinebank.entity.Loan;
import com.onlinebank.exception.ResourceNotFoundException;
import com.onlinebank.exception.UnauthorizedException;
import com.onlinebank.repository.BankUserRepository;
import com.onlinebank.repository.LoanRepository;
import com.onlinebank.service.LoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
 
    @Autowired
    private LoanRepository loanRepository;
 
    @Autowired
    private BankUserRepository userRepository;
 
    @Override
    public Loan applyLoanForCurrentUser(Loan loanRequest, String userEmail) {
        // Fetch the logged-in user by email
        BankUser user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));
 
        // Set the user for the loan
        loanRequest.setBankUser(user);
        
        // Set the loan status to PENDING explicitly
        loanRequest.setStatus("PENDING");
 
        // Save the loan
        return loanRepository.save(loanRequest);
    }
 
    @Override
    public LoanDTO updateLoanForCurrentUser(Long loanId, Loan loanRequest, String userEmail) {
        // Fetch the existing loan
        Loan existingLoan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));
 
        // Ensure the loan belongs to the logged-in user
        if (!existingLoan.getBankUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You do not have access to this loan.");
        }
 
        // Update loan details
        existingLoan.setAmount(loanRequest.getAmount());
//        existingLoan.setStatus(loanRequest.getStatus());
 
        // Save the updated loan
        Loan updatedLoan = loanRepository.save(existingLoan);
 
        // Map to DTO
        return mapToDTO(updatedLoan);
    }
 
    @Override
    public void deleteLoanForCurrentUser(Long loanId, String userEmail) {
        // Fetch the loan
        Loan existingLoan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));
 
        // Ensure the loan belongs to the logged-in user
        if (!existingLoan.getBankUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You do not have access to this loan.");
        }
 
        // Delete the loan
        loanRepository.delete(existingLoan);
    }
 
    @Override
    public List<LoanDTO> getLoansForCurrentUser(String userEmail) {
        // Fetch all loans and filter by the logged-in user's email
        return loanRepository.findAll().stream()
                .filter(loan -> loan.getBankUser().getEmail().equals(userEmail))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
 
    private LoanDTO mapToDTO(Loan loan) {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setAmount(loan.getAmount());
        loanDTO.setStatus(loan.getStatus());
        loanDTO.setBankUserName(loan.getBankUser().getName());
        loanDTO.setId(loan.getId());
        return loanDTO;
    }
}
