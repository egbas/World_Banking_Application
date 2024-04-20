package com.egbas.World.Banking.payload.response;

import com.egbas.World.Banking.domain.entities.ErrorDetails;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor

public class BankResponse<T> {
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;

    public BankResponse(String responseCode, String responseMessage, AccountInfo accountInfo) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.accountInfo = accountInfo;
    }

    public BankResponse(String message) {
        this.responseMessage = responseMessage;
    }

    public BankResponse(String responseMessage, ErrorDetails errorDetails) {
        this.responseMessage = responseMessage;

    }

    public BankResponse(String message, String fileUrl) {
        this.responseMessage = message;
    }

}
