package com.playtomic.tests.wallet.infrastructure.charger;

import com.playtomic.tests.wallet.domain.charger.Charge;
import com.playtomic.tests.wallet.domain.charger.ChargeException;
import com.playtomic.tests.wallet.domain.charger.CreditCard;
import com.playtomic.tests.wallet.domain.charger.CreditCardCharger;
import com.playtomic.tests.wallet.domain.exception.ValidationException;
import com.playtomic.tests.wallet.service.Payment;
import com.playtomic.tests.wallet.service.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component("stripeCreditCardCharger")
public final class StripeCreditCardCharger implements CreditCardCharger {
  private final StripeService stripeService;

  public StripeCreditCardCharger(StripeService stripeService) {
    this.stripeService = stripeService;
  }

  @Override
  public Charge charge(CreditCard creditCard, BigDecimal amount) {
    try {
      Payment payment = stripeService.charge(creditCard.getNumber(), amount);
      return new Charge(payment.getId());
    } catch (StripeAmountTooSmallException ex) {
      throw new ValidationException("Amount too short");
    } catch (StripeServiceException ex) {
      throw new ChargeException();
    }
  }
}
