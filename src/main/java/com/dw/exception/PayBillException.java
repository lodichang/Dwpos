package com.dw.exception;

/**
 * 付款異常
 */
public class PayBillException extends RuntimeException {

    public PayBillException(String message) {
        super(message);
    }

    public PayBillException(String message, Throwable cause) {
        super(message, cause);
    }
}

