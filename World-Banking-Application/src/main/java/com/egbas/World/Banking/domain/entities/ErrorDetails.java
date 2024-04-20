package com.egbas.World.Banking.domain.entities;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private String message;
    private String debugMessage;
    private HttpStatus status;
    private String dateTime;
}
