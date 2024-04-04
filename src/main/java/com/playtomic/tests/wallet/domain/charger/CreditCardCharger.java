package com.playtomic.tests.wallet.domain.charger;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface CreditCardCharger {

    void charge(CreditCard creditCard, BigDecimal amount);
}
