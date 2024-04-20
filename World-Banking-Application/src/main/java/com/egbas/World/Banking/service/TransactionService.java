package com.egbas.World.Banking.service;

import com.egbas.World.Banking.payload.request.TransactionRequest;

public interface TransactionService {
    void saveTransactions(TransactionRequest transactionRequest);
}
