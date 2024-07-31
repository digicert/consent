package com.digicert.consent.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LogSessionTest {

    @Mock
    private LogState logState;

    @InjectMocks
    private LogSession logSession;

    @Test
    public void setTest() {
        logSession.set(logState);
    }

    @Test
    public void getTest() {
        logSession.get();
    }

    @Test
    public void removeTest() {
        logSession.remove();
    }
}
