package com.digicert.consent.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LogStateTest {


    @InjectMocks
    private LogState logState;

    @Test
    public void setTest() {
        logState.getTransactionId();
    }

    @Test
    public void getTest() {
        logState.setTransactionId("transactionId");
    }

    @Test
    public void removeTest() {
        logState.resetTransactionId();
    }

    @Test
    public void resetTransactionIdTest() {
        logState.resetTransactionId();
    }



}
