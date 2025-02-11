//package com.onlinebank.service.impl;
//
//import com.onlinebank.dto.AccountDTO;
//import com.onlinebank.dto.BankUserDTO;
//import com.onlinebank.dto.TransactionDTO;
//import com.onlinebank.entity.Account;
//import com.onlinebank.entity.BankUser;
//import com.onlinebank.entity.Transaction;
//import com.onlinebank.exception.ResourceNotFoundException;
//import com.onlinebank.repository.AccountRepository;
//import com.onlinebank.repository.BankUserRepository;
//import com.onlinebank.repository.TransactionRepository;
//import com.onlinebank.service.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class AdminServiceImpl implements AdminService {
//
//    @Autowired
//    private BankUserRepository userRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//    
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public List<BankUserDTO> getAllUsers() {
//        return userRepository.findAll().stream()
//                .map(user -> {
//                    BankUserDTO dto = new BankUserDTO();
//                    dto.setName(user.getName());
//                    dto.setId(user.getId());
//                    dto.setEmail(user.getEmail());
//                    dto.setMobileNumber(user.getMobileNumber());
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public BankUserDTO getUserById(Long id) {
//        BankUser user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
//        BankUserDTO dto = new BankUserDTO();
//        dto.setName(user.getName());
//        dto.setId(user.getId());
//        dto.setEmail(user.getEmail());
//        dto.setMobileNumber(user.getMobileNumber());
//        return dto;
//    }
//    
//    
//    @Override
//    public AccountDTO updateAccountBalance(Long accountId, Double newBalance) {
//        Account account = accountRepository.findById(accountId)
//                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));
//     
//        account.setBalance(newBalance);
//        Account updatedAccount = accountRepository.save(account);
//     
//        return mapToDTO(updatedAccount);
//    }
//     
//    private AccountDTO mapToDTO(Account account) {
//        AccountDTO dto = new AccountDTO();
//        dto.setAccountNumber(account.getAccountNumber());
//        dto.setBalance(account.getBalance());
//        dto.setBankUserName(account.getBankUser().getName());
//        dto.setEmail(account.getBankUser().getEmail());
//        dto.setId(account.getId());
//        return dto;
//    }
//     
//
//    @Override
//    public List<AccountDTO> getAllAccounts() {
//        return accountRepository.findAll().stream()
//                .map(account -> {
//                    AccountDTO dto = new AccountDTO();
//                    dto.setAccountNumber(account.getAccountNumber());
//                    dto.setBalance(account.getBalance());
//                    dto.setBankUserName(account.getBankUser().getName());
//                    dto.setEmail(account.getBankUser().getEmail());
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<TransactionDTO> getAllTransactions() {
//        return transactionRepository.findAll().stream()
//                .map(transaction -> {
//                    TransactionDTO dto = new TransactionDTO();
//                    dto.setTransactionId(transaction.getId());
//                    dto.setTransactionDateTime(transaction.getTransactionDateTime());
//                    dto.setTransactionType(transaction.getTransactionType());
//                    dto.setAmount(transaction.getAmount());
//                    dto.setAccountNumber(transaction.getAccount().getAccountNumber());
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }
//    
//    @Override
//    public BankUserDTO updateUser(Long id, BankUser updatedUser) {
//        BankUser existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
//        
//        // Update the existing user's details
//        existingUser.setName(updatedUser.getName());
//        existingUser.setEmail(updatedUser.getEmail());
//        existingUser.setAadharNumber(updatedUser.getAadharNumber());
//        existingUser.setMobileNumber(updatedUser.getMobileNumber());
//        
//        // Optionally update the password (encrypt it first)
//        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
//            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
//        }
//
//        // Save the updated user back to the database
//        BankUser savedUser = userRepository.save(existingUser);
//
//        // Map the updated user to a DTO and return it
//        BankUserDTO userDTO = new BankUserDTO();
//        userDTO.setName(savedUser.getName());
//        userDTO.setId(savedUser.getId());
//        userDTO.setEmail(savedUser.getEmail());
//        userDTO.setMobileNumber(savedUser.getMobileNumber());
//        return userDTO;
//    }
//    
//    @Override
//    public void deleteUser(Long id) {
//        BankUser existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
//        userRepository.delete(existingUser);
//    }
//
//
//}

package com.onlinebank.service.impl;

import com.onlinebank.dto.AccountDTO;
import com.onlinebank.dto.BankUserDTO;
import com.onlinebank.dto.LoanDTO;
import com.onlinebank.dto.TransactionDTO;
import com.onlinebank.entity.Account;
import com.onlinebank.entity.BankUser;
import com.onlinebank.entity.Loan;
import com.onlinebank.entity.Transaction;
import com.onlinebank.exception.ResourceNotFoundException;
import com.onlinebank.repository.AccountRepository;
import com.onlinebank.repository.BankUserRepository;
import com.onlinebank.repository.LoanRepository;
import com.onlinebank.repository.TransactionRepository;
import com.onlinebank.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private BankUserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    /** ====================================
     *  Loan approval and crediding and transcation of that loan approval
     *  ==================================== */
    
    @Autowired
    private LoanRepository loanRepository;
    public void processLoanApproval(Long loanId, boolean approve, String adminEmail) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));

        if (!loan.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Loan is already processed.");
        }

        if (approve) {
            // ✅ Fetch all accounts of the user
            List<Account> userAccounts = accountRepository.findByBankUserId(loan.getBankUser().getId());

            // ✅ Ensure user has an account
            if (userAccounts.isEmpty()) {
                throw new ResourceNotFoundException("User has no bank account.");
            }

            // ✅ Select the first account to credit the loan
            Account userAccount = userAccounts.get(0);

            // ✅ Credit the loan amount
            userAccount.setBalance(userAccount.getBalance() + loan.getAmount());
            accountRepository.save(userAccount);

            // ✅ Save transaction record
            Transaction transaction = new Transaction();
            transaction.setAmount(loan.getAmount());
            transaction.setTransactionType("LOAN CREDIT");
            transaction.setAccount(userAccount);
            transaction.setTransactionDateTime(LocalDateTime.now());
            transactionRepository.save(transaction);

            // ✅ Update loan details
            loan.setStatus("APPROVED");
            loan.setApprovedBy(adminEmail);
            loan.setApprovalDate(LocalDateTime.now());
        } else {
            loan.setStatus("REJECTED");
        }

        loanRepository.save(loan);
    }
    
    /** ✅ Get all loans for admin */
    @Override
    public List<LoanDTO> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /** ✅ Get a loan by ID */
    @Override
    public LoanDTO getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));
        return mapToDTO(loan);
    }

    /** 🔹 Helper method to map `Loan` entity to `LoanDTO` */
    private LoanDTO mapToDTO(Loan loan) {
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setId(loan.getId());
        loanDTO.setAmount(loan.getAmount());
        loanDTO.setStatus(loan.getStatus());
        loanDTO.setApprovedBy(loan.getApprovedBy());
        loanDTO.setApprovalDate(loan.getApprovalDate());
        loanDTO.setBankUserName(loan.getBankUser().getName());
        return loanDTO;
    }


    /** ====================================
     *  USER MANAGEMENT
     *  ==================================== */
    
    @Override
    public List<BankUserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToBankUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BankUserDTO getUserById(Long id) {
        BankUser user = findUserById(id);
        return mapToBankUserDTO(user);
    }

    @Override
    public BankUserDTO updateUser(Long id, BankUser updatedUser) {
        BankUser existingUser = findUserById(id);

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
//        existingUser.setAadharNumber(updatedUser.getAadharNumber());
        existingUser.setMobileNumber(updatedUser.getMobileNumber());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return mapToBankUserDTO(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(findUserById(id));
    }

    /** ====================================
     *  ACCOUNT MANAGEMENT
     *  ==================================== */

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(this::mapToAccountDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO updateAccountBalance(Long accountId, Double newBalance) {
        Account account = findAccountById(accountId);
        account.setBalance(newBalance);
        return mapToAccountDTO(accountRepository.save(account));
    }

    /** ====================================
     *  TRANSACTION MANAGEMENT
     *  ==================================== */

    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToTransactionDTO)
                .collect(Collectors.toList());
    }

    /** ====================================
     *  HELPER METHODS
     *  ==================================== */

    private BankUser findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + id));
    }

    private BankUserDTO mapToBankUserDTO(BankUser user) {
        BankUserDTO dto = new BankUserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        return dto;
    }

    private AccountDTO mapToAccountDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setBankUserName(account.getBankUser().getName());
        dto.setEmail(account.getBankUser().getEmail());
        return dto;
    }

    private TransactionDTO mapToTransactionDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(transaction.getId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionDateTime(transaction.getTransactionDateTime());
        dto.setAccountNumber(transaction.getAccount().getAccountNumber());
        return dto;
    }
}
