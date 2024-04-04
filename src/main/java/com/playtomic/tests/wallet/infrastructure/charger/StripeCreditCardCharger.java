package com.playtomic.tests.wallet.infrastructure.charger;

import com.playtomic.tests.wallet.domain.charger.CreditCard;
import com.playtomic.tests.wallet.domain.charger.CreditCardCharger;
import com.playtomic.tests.wallet.service.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.StripeService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("stripeCreditCardCharger")
public final class StripeCreditCardCharger implements CreditCardCharger {
    private final StripeService stripeService;

    public StripeCreditCardCharger(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @Override
    public void charge(CreditCard creditCard, BigDecimal amount) {
        try {
            stripeService.charge(creditCard.getNumber(), amount);
        } catch (StripeAmountTooSmallException ex) {
            throw new IllegalArgumentException("Amount too short");
        }
    }
}
