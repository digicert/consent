package com.digicert.consent.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogState {

    private String transactionId;

    public String getTransactionId() {
        if (transactionId == null) {
            this.transactionId = generateTransactionId();
        }
        return transactionId;
    }

    public void resetTransactionId() {
        this.transactionId = generateTransactionId();
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

