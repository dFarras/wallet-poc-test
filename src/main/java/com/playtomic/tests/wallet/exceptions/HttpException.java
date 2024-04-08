package com.playtomic.tests.wallet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HttpException extends RuntimeException {
    private static final String DEFAULT_CLARIFIED_MSG = "There is no particular clarification for this error";
    private HttpStatus httpStatus;
    private String code;
    private String genericMessage;
    private String clarifiedMessage;

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
