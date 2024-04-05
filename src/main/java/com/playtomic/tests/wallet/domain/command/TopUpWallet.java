package com.playtomic.tests.wallet.domain.command;

import com.playtomic.tests.wallet.domain.NotFoundException;
import com.playtomic.tests.wallet.domain.charger.Charge;
import com.playtomic.tests.wallet.domain.charger.CreditCard;
import com.playtomic.tests.wallet.domain.charger.CreditCardCharger;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public final class TopUpWallet {
  private final CreditCardCharger creditCardCharger;
  private final CreateWalletTopUp createWalletTopUp;
  private final WalletRepository walletRepository;

  public TopUpWallet(
      @Qualifier("stripeCreditCardCharger") CreditCardCharger creditCardCharger,
      CreateWalletTopUp createWalletTopUp,
      WalletRepository walletRepository) {
    this.creditCardCharger = creditCardCharger;
    this.createWalletTopUp = createWalletTopUp;
    this.walletRepository = walletRepository;
  }

  public Result execute(Input input) {
    AtomicLong topUpId = new AtomicLong();

    walletRepository
        .update(
            input.walletId,
            wallet -> {
              wallet.topUp(input.amount);

              Charge charge =
                  creditCardCharger.charge(new CreditCard(input.creditCardNumber), input.amount);

              long id =
                  createWalletTopUp.execute(
                      CreateWalletTopUp.Input.builder()
                          .paymentId(charge.getPaymentId())
                          .walletId(wallet.getId())
                          .amount(input.amount)
                          .build());

              topUpId.set(id);
            })
        .orElseThrow(NotFoundException::new);

    return new Result(topUpId.get());
  }

  @Value
  @Builder
  public static class Input {
    @NonNull Long walletId;

    @NonNull String creditCardNumber;

    @NonNull BigDecimal amount;
  }

  @Value
  public static class Result {
    @NonNull Long topUpId;
  }
}
