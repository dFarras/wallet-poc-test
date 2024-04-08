package com.playtomic.tests.wallet.service;

import lombok.NonNull;

import java.math.BigDecimal;

public interface PaymentGateway {
    Payment charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount) throws StripeServiceException;
    void refund(@NonNull String paymentId) throws StripeServiceException;
}
