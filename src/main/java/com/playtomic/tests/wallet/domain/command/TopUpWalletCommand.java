package com.playtomic.tests.wallet.domain.command;

import com.playtomic.tests.wallet.domain.charger.Charge;
import com.playtomic.tests.wallet.domain.charger.CreditCard;
import com.playtomic.tests.wallet.domain.charger.CreditCardCharger;
import com.playtomic.tests.wallet.domain.command.entity.WalletTopUp;
import com.playtomic.tests.wallet.domain.command.repository.WalletRepository;
import com.playtomic.tests.wallet.domain.command.repository.WalletTopUpRepository;
import com.playtomic.tests.wallet.domain.exception.NotFoundException;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public final class TopUpWalletCommand {
  private final CreditCardCharger creditCardCharger;
  private final WalletRepository walletRepository;
  private final WalletTopUpRepository walletTopUpRepository;

  public TopUpWalletCommand(
      @Qualifier("stripeCreditCardCharger") CreditCardCharger creditCardCharger,
      @Qualifier("databaseWalletRepository") WalletRepository walletRepository,
      @Qualifier("databaseWalletTopUpRepository") WalletTopUpRepository walletTopUpRepository) {
    this.creditCardCharger = creditCardCharger;
    this.walletRepository = walletRepository;
    this.walletTopUpRepository = walletTopUpRepository;
  }

  public Result execute(Input input) {
    long topUpId = walletTopUpRepository.nextId();

    walletRepository
        .update(
            input.walletId,
            wallet -> {
              Charge charge =
                  creditCardCharger.charge(new CreditCard(input.creditCardNumber), input.amount);

              WalletTopUp walletTopUp =
                  new WalletTopUp(topUpId, charge.getPaymentId(), input.amount);

              wallet.topUp(walletTopUp);
            })
        .orElseThrow(NotFoundException::new);

    return new Result(topUpId);
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
