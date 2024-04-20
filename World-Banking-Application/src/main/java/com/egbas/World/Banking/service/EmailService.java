package com.egbas.World.Banking.service;

import com.egbas.World.Banking.payload.request.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}
