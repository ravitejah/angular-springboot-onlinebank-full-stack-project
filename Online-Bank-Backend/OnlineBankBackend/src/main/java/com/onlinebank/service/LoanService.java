package com.onlinebank.service;

import com.onlinebank.dto.LoanDTO;
import com.onlinebank.entity.Loan;

import java.util.List;

public interface LoanService {
    Loan applyLoanForCurrentUser(Loan loanRequest, String userEmail);
    LoanDTO updateLoanForCurrentUser(Long loanId, Loan loanRequest, String userEmail);
    void deleteLoanForCurrentUser(Long loanId, String userEmail);
    List<LoanDTO> getLoansForCurrentUser(String userEmail);
}
