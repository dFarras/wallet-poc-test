package com.playtomic.tests.wallet.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.configuration.StripeConfiguration;
import com.playtomic.tests.wallet.exceptions.HttpException;
import com.playtomic.tests.wallet.service.Payment;
import com.playtomic.tests.wallet.service.StripeRestTemplateResponseErrorHandler;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * This test is failing with the current implementation.
 * <p>
 * How would you test this?
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        HttpException.class,
        StripeService.class,
        StripeConfiguration.class,
        StripeRestTemplateResponseErrorHandler.class})
@RestClientTest
public class StripeServiceIT {
    private static final URI TEST_URI = URI.create("https://sandbox.playtomic.io/v1/stripe-simulator/charges");
    private static final String EXPECTED_CODE = "INVALID-002";
    private static final String EXPECTED_MESSAGE = "Payment gateway only accepts charges over 10€.";
    private static final String EXPECTED_CLARIFICATION = "For charges less than 10€ please choose a different payment gateway";

    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StripeService stripeService;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void init() {
        server = MockRestServiceServer.createServer(this.restTemplate);
    }

    @Test
    public void test_exception() {
        this.server
                .expect(ExpectedCount.once(), requestTo(TEST_URI))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.UNPROCESSABLE_ENTITY));

        final HttpException httpException = Assertions.assertThrows(HttpException.class, () -> {
            this.stripeService.charge("4242 4242 4242 4242", new BigDecimal(5));
        });

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, httpException.getHttpStatus());
        Assertions.assertEquals(EXPECTED_CODE, httpException.getCode());
        Assertions.assertEquals(EXPECTED_MESSAGE, httpException.getGenericMessage());
        Assertions.assertEquals(EXPECTED_CLARIFICATION, httpException.getClarifiedMessage());

    }

    @Test
    public void test_ok() throws StripeServiceException, JsonProcessingException {
        final Payment expectation = new Payment("some-id");

        this.server
                .expect(ExpectedCount.once(), requestTo(TEST_URI))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(this.objectMapper.writeValueAsString(expectation), MediaType.APPLICATION_JSON))
        ;

        final Payment payment = this.stripeService.charge("4242 4242 4242 4242", new BigDecimal(15));

        Assertions.assertEquals(expectation, payment);
    }
}
