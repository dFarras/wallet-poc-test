package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.exceptions.ErrorCatalog;
import com.playtomic.tests.wallet.exceptions.HttpException;
import lombok.extern.slf4j.Slf4j;
import javax.validation.ConstraintViolationException;
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
        log.info(EXCEPTION_DETAIL, httpException);
        return new ResponseEntity<>(exceptionResponse, httpException.getHttpStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpExceptionResponse> handleIllegalArgumentException(
            final IllegalArgumentException illegalArgumentException) {
        final ErrorCatalog error = ErrorCatalog.GENERIC_BAD_REQUEST;
        final HttpExceptionResponse exceptionResponse = HttpExceptionResponse.builder()
                .code(error.getCode())
                .genericMessage(error.getMessage())
                .build();
        log.info(EXCEPTION_DETAIL, illegalArgumentException);
        return new ResponseEntity<>(exceptionResponse, error.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpExceptionResponse> handleConstraintException(
            final ConstraintViolationException constraintViolationException
    ) {
        final ErrorCatalog error = ErrorCatalog.GENERIC_BAD_REQUEST;
        final HttpExceptionResponse exceptionResponse = HttpExceptionResponse.builder()
                .code(error.getCode())
                .genericMessage(error.getMessage())
                .build();
        log.info(EXCEPTION_DETAIL, constraintViolationException);
        return new ResponseEntity<>(exceptionResponse, error.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpExceptionResponse> handleException(
            final Exception exception) {
        final ErrorCatalog error = ErrorCatalog.INTERNAL_SERVER_ERROR;
        final HttpExceptionResponse exceptionResponse = HttpExceptionResponse.builder()
                .code(error.getCode())
                .genericMessage(error.getMessage())
                .build();
        log.info(EXCEPTION_DETAIL, exception);
        return new ResponseEntity<>(exceptionResponse, error.getHttpStatus());
    }

}
