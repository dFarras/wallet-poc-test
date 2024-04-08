package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exceptions.ErrorCatalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class StripeRestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        final HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        if (Objects.isNull(statusCode)) {
            log.error("Could not extract error code from the response");
            throw ErrorCatalog.INTERNAL_SERVER_ERROR.getException();
        }
        if (statusCode.is4xxClientError()) {
            /**
             * I could definitely validate the amount at service level, but I think it is not a good idea since we can
             * have more than one implementation and not all would have this limitation
             */
            if (statusCode == HttpStatus.UNPROCESSABLE_ENTITY) {
                log.error("The amount is less than 10€");
                /**
                 * I have decided to implement this assuming that the user could select a different payment gateway,
                 * otherwise it would be pointless to show the implementation used
                 */
                throw ErrorCatalog.FEW_FUNDS.getException("For charges less than 10€ please choose a different payment gateway");
            } else {
                log.error("Stripe failed due to an invalid request");
                throw ErrorCatalog.GENERIC_BAD_REQUEST.getException();
            }
        } else if (statusCode.is5xxServerError()) {
            log.error("Stripe failed unexpectedly");
            throw ErrorCatalog.PAYMENT_GATEWAY_UNAVAILABLE.getException();
        } else {
            log.error("unexpected error raised an exception");
            throw ErrorCatalog.INTERNAL_SERVER_ERROR.getException();
        }
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        int rawStatusCode = response.getRawStatusCode();
        HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
        return (statusCode != null ? statusCode.isError() : hasError(rawStatusCode));
    }

    private boolean hasError(int unknownStatusCode) {
        HttpStatus.Series series = HttpStatus.Series.resolve(unknownStatusCode);
        return (series == HttpStatus.Series.CLIENT_ERROR || series == HttpStatus.Series.SERVER_ERROR);
    }
}
