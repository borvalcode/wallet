package com.playtomic.tests.wallet.infrastructure.charger;

import com.playtomic.tests.wallet.domain.charger.CreditCard;
import com.playtomic.tests.wallet.domain.charger.CreditCardCharger;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component("testCreditCardCharger")
public class TestCreditCardCharger implements CreditCardCharger {
  @Override
  public void charge(CreditCard creditCard, BigDecimal amount) {}
}
