package com.egbas.World.Banking.infrastructure.exception;

public class EmailNotSentException extends RuntimeException{
    public EmailNotSentException(String message) {
        super(message);
    }
}
