package com.playtomic.tests.wallet.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;


/**
 * Handles the communication with Stripe.
 * <p>
 * A real implementation would call to String using their API/SDK.
 * This dummy implementation throws an error when trying to charge less than 10€.
 */
@Service
public class StripeService implements PaymentGateway {

    @NonNull
    private URI chargesUri;

    @NonNull
    private URI refundsUri;

    @NonNull
    private RestTemplate restTemplate;

    public StripeService(@Value("${stripe.simulator.charges-uri}") @NonNull URI chargesUri,
                         @Value("${stripe.simulator.refunds-uri}") @NonNull URI refundsUri,
                         @Qualifier("stripeRestTemplate") RestTemplate restTemplate) {
        this.chargesUri = chargesUri;
        this.refundsUri = refundsUri;
        this.restTemplate = restTemplate;
    }

    /**
     * Charges money in the credit card.
     * <p>
     * Ignore the fact that no CVC or expiration date are provided.
     *
     * @param creditCardNumber The number of the credit card
     * @param amount           The amount that will be charged.
     * @throws StripeServiceException
     */
    public Payment charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount) throws StripeServiceException {
        ChargeRequest body = new ChargeRequest(creditCardNumber, amount);
        return this.restTemplate.postForObject(this.chargesUri, body, Payment.class);
    }

    /**
     * Refunds the specified payment.
     */
    public void refund(@NonNull String paymentId) throws StripeServiceException {
        // Object.class because we don't read the body here.
        /**
         * Maybe I am wrong, but it looks like this url is incorrect, so I fixed it. In a real scenario I would check
         * whether it is correct or not.
         * */
        this.restTemplate.postForEntity(this.refundsUri.toString(), null, Object.class, paymentId);
    }

    @AllArgsConstructor
    private static class ChargeRequest {

        @NonNull
        @JsonProperty("credit_card")
        String creditCardNumber;

        @NonNull
        @JsonProperty("amount")
        BigDecimal amount;
    }
}
