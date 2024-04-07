package com.playtomic.tests.wallet.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class HttpException extends RuntimeException {
    private static final String DEFAULT_CLARIFIED_MSG = "There is no particular clarification for this error";
    private final HttpStatus httpStatus;
    private final String code;
    private final String genericMessage;
    private final String clarifiedMessage;

    public HttpException(
            final HttpStatus httpStatus,
            final String code,
            final String genericMessage
    ) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.genericMessage = genericMessage;
        this.clarifiedMessage = DEFAULT_CLARIFIED_MSG;
    }
}
