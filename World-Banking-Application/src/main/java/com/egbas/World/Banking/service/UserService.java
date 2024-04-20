package com.egbas.World.Banking.service;

import com.egbas.World.Banking.payload.request.CreditAndDebitRequest;
import com.egbas.World.Banking.payload.request.EnquiryRequest;
import com.egbas.World.Banking.payload.request.TransferRequest;
import com.egbas.World.Banking.payload.response.BankResponse;

public interface UserService {
    BankResponse creditAccount(CreditAndDebitRequest request);

    BankResponse debitAccount(CreditAndDebitRequest request);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse transfer(TransferRequest request);
}
