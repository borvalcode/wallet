package com.playtomic.tests.wallet.domain.charger;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public interface CreditCardCharger {

  void charge(CreditCard creditCard, BigDecimal amount);
}
