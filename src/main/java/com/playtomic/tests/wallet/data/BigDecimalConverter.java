package com.playtomic.tests.wallet.data;

import com.playtomic.tests.wallet.exceptions.ErrorCatalog;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
@Validated
public class BigDecimalConverter {
    private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);

    public BigDecimal from(@NotNull String value) {
        try {
            return new BigDecimal(value, MATH_CONTEXT);
        } catch (NumberFormatException e) {
            throw ErrorCatalog.NOT_A_NUMBER.getException();
        }
    }

    public String from(@NotNull BigDecimal result) {
        return result.toPlainString();
    }
}
