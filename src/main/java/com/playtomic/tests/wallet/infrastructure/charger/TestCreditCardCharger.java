package com.playtomic.tests.wallet.infrastructure.charger;

import com.playtomic.tests.wallet.domain.charger.CreditCard;
import com.playtomic.tests.wallet.domain.charger.CreditCardCharger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("testCreditCardCharger")
public class TestCreditCardCharger implements CreditCardCharger {
    @Override
    public void charge(CreditCard creditCard, BigDecimal amount) {
    }
}
