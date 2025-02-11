package com.onlinebank.controller;

import com.onlinebank.dto.LoanDTO;
import com.onlinebank.entity.Loan;
import com.onlinebank.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.onlinebank.exception.UnauthorizedException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loans")
public class LoanController {
 
    @Autowired
    private LoanService loanService;
 
    @PostMapping
    public LoanDTO applyLoan(@RequestBody Loan loanRequest, Authentication authentication) {
        // Fetch the logged-in user's email
        String currentUserEmail = authentication.getName();
 
        // Apply for a loan for the logged-in user
        Loan createdLoan = loanService.applyLoanForCurrentUser(loanRequest, currentUserEmail);
        return mapToDTO(createdLoan);
    }
 
    @PutMapping("/{id}")
    public LoanDTO updateLoan(@PathVariable Long id, @RequestBody Loan loanRequest, Authentication authentication) {
        // Fetch the logged-in user's email
        String currentUserEmail = authentication.getName();
 
        // Update loan details for the logged-in user
        return loanService.updateLoanForCurrentUser(id, loanRequest, currentUserEmail);
         
    }
 
    @DeleteMapping("/{id}")
    public String deleteLoan(@PathVariable Long id, Authentication authentication) {
        // Fetch the logged-in user's email
        String currentUserEmail = authentication.getName();
 
        // Delete the loan for the logged-in user
        loanService.deleteLoanForCurrentUser(id, currentUserEmail);
        return "Loan deleted successfully!";
    }
 
    @GetMapping
    public List<LoanDTO> getAllLoans(Authentication authentication) {
        // Fetch the logged-in user's email
        String currentUserEmail = authentication.getName();
 
        // Return all loans belonging to the logged-in user
        return loanService.getLoansForCurrentUser(currentUserEmail);
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

