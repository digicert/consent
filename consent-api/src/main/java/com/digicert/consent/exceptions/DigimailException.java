package com.digicert.consent.exceptions;

public class DigimailException extends RuntimeException {

    public DigimailException() {
        super();
    }

    public DigimailException(String message) {
        super(message);
    }

    public DigimailException(Throwable cause) {
        super(cause);
    }

    public DigimailException(String message, Throwable cause) {
        super(message, cause);
    }

    public DigimailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
