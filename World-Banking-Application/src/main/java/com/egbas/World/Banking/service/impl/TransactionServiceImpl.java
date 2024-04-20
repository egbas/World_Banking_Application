package com.egbas.World.Banking.service.impl;

import com.egbas.World.Banking.domain.entities.Transaction;
import com.egbas.World.Banking.payload.request.TransactionRequest;
import com.egbas.World.Banking.repository.TransactionRepository;
import com.egbas.World.Banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    @Override
    public void saveTransactions(TransactionRequest transactionRequest) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionRequest.getTransactionType())
                .accountNumber(transactionRequest.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);

        System.out.println("Transaction Saved Successfully!");

    }
}
