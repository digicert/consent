package com.digicert.consent.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LogUtilsTest {

    @InjectMocks
    private LogUtils logUtils;

    @Test
    public void prependTransactionIdToObjectsTest() {
        LogUtils.prependTransactionIdToObjects(logUtils);
    }

    @Test
    public void prependTransactionIdToStrTest() {
        LogUtils.prependTransactionIdToStr("str");
    }

    @Test
    public void debugTest() {
        LogUtils.debug("message", logUtils);
    }

    @Test
    public void infoTest() {
        LogUtils.info("message", logUtils);
    }

    @Test
    public void warnTest() {
        LogUtils.warn("message", logUtils);
    }

    @Test
    public void errorTest() {
        LogUtils.error("message", logUtils);
    }

}
