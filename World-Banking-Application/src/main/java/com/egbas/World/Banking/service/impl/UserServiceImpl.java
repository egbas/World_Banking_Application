package com.egbas.World.Banking.service.impl;

import com.egbas.World.Banking.domain.entities.UserEntity;
import com.egbas.World.Banking.payload.request.*;
import com.egbas.World.Banking.payload.response.AccountInfo;
import com.egbas.World.Banking.payload.response.BankResponse;
import com.egbas.World.Banking.repository.UserRepository;
import com.egbas.World.Banking.service.EmailService;
import com.egbas.World.Banking.service.TransactionService;
import com.egbas.World.Banking.service.UserService;
import com.egbas.World.Banking.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TransactionService transactionService;
    @Override
    public BankResponse creditAccount(CreditAndDebitRequest request) {

        //to credit an account first check if the account exists

        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
            if (!isAccountExists){
                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_EXIST_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                        .accountInfo(null)
                        .build();
            }
        UserEntity userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
            userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));


            userRepository.save(userToCredit);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(userToCredit.getEmail())
                .messageBody("Your account has been credited with " + request.getAmount() +
                        " from " + userToCredit.getFirstName() + " Your current account balance is " +
                        userToCredit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransactions(transactionRequest);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + userToCredit.getMiddleName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditAndDebitRequest request) {

        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        if(availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of " + request.getAmount() +
                            "has been deducted from your account! Your current account balance is "  +
                            userToDebit.getAccountBalance())
                    .build();
            emailService.sendEmailAlert(debitAlert);

            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(request.getAmount())
                    .build();
            transactionService.saveTransactions(transactionRequest);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getMiddleName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(request.getAccountNumber())
                            .build())
                    .build();
        }

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NUMBER_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NUMBER_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getMiddleName())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if(!isAccountExists){
            return AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE;
        }
        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getMiddleName();
    }

    @Override
    public BankResponse transfer(TransferRequest request) {

        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if(!isDestinationAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());

        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);

        String sourceUserName = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName() + sourceAccountUser.getMiddleName();

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of " + request.getAmount() +
                        "has been deducted from your account! Your current account balance is "  +
                        sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        UserEntity destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("Your account has been credited with " + request.getAmount() + " " + sourceUserName + " "  + "Your current account balance is " + destinationAccountUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(creditAlert);

        //save transfer transaction
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("TRANSFER")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransactions(transactionRequest);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
