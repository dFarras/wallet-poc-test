package com.playtomic.tests.wallet.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    GENERIC_BAD_REQUEST(HttpStatus.BAD_REQUEST, "INVALID-001", "Invalid request, please check all fields are valid."),
    FEW_FUNDS(HttpStatus.BAD_REQUEST, "INVALID-002", "Payment gateway only accepts charges over 10€."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "INVALID-003", "Requested resource does not exist"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL-001", "Uncontrolled error."),
    PAYMENT_GATEWAY_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "INTERNAL-002", "Payment gateway is unavailable, please try again later.");
    //CRITICAL_PAYMENT_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "INTERNAL-002", "Payment gateway is unavailable, please try again later.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public HttpException getException() {
        log.error(String.format("HTTP STATUS: %s CODE: %s MESSAGE: %s", this.httpStatus, this.code, this.message));
        return new HttpException(this.httpStatus, this.code, this.message);
    }

    public HttpException getException(final String clarifiedMessage) {
        log.error(String.format("HTTP STATUS: %s CODE: %s MESSAGE: %s CLARIFIED: %s", this.httpStatus, this.code, this.message,
                clarifiedMessage));
        return new HttpException(this.httpStatus, this.code, this.message, clarifiedMessage);
    }

}
