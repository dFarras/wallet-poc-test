package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.exceptions.ErrorCatalog;
import com.playtomic.tests.wallet.exceptions.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class HttpExceptionController {
    public static final String EXCEPTION_DETAIL = "Exception detail";

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<HttpExceptionResponse> handleHttpException(final HttpException httpException) {
        final HttpExceptionResponse exceptionResponse = HttpExceptionResponse.builder()
                .code(httpException.getCode())
                .genericMessage(httpException.getGenericMessage())
                .clarifiedMessage(httpException.getClarifiedMessage())
                .build();
        log.error(EXCEPTION_DETAIL, httpException);
        return new ResponseEntity<>(exceptionResponse, httpException.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpExceptionResponse> handleInvalidReferenceException(
            final Exception invalidReferenceException) {
        final ErrorCatalog error = ErrorCatalog.INTERNAL_SERVER_ERROR;
        final HttpExceptionResponse exceptionResponse = HttpExceptionResponse.builder()
                .code(error.getCode())
                .genericMessage(error.getMessage())
                .build();
        log.error(EXCEPTION_DETAIL, invalidReferenceException);
        return new ResponseEntity<>(exceptionResponse, error.getHttpStatus());
    }

}
