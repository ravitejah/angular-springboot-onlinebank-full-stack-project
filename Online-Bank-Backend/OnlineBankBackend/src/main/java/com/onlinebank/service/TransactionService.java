package com.onlinebank.service;

import com.onlinebank.dto.TransactionDTO;
import com.onlinebank.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction performTransaction(Long accountId, Transaction transaction);

    TransactionDTO getTransactionById(Long transactionId);

    List<TransactionDTO> getAllTransactions();
}
