package com.wallet.parser.service.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Unchecked exception to be thrown as higher exception in order to decouple the exception management in a multitier application.
 * Created by Liodegar on 11/20/2018.
 */
public class ParserException extends NestedRuntimeException {

    /**
     * Constructs a ParserException with the specified detail message.
     *
     * @param message Message intended for developers.
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Constructs a ParserException with the specified detail message and the original exception.
     *
     * @param message           Message intended for developers.
     * @param originalException The nested exception.
     */
    public ParserException(String message, Throwable originalException) {
        super(message, originalException);
    }

}
