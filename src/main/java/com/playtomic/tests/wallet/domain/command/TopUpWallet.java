package com.playtomic.tests.wallet.domain.command;

import com.playtomic.tests.wallet.domain.charger.CreditCard;
import com.playtomic.tests.wallet.domain.charger.CreditCardCharger;
import com.playtomic.tests.wallet.domain.WalletNotFoundException;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TopUpWallet {
    private final CreditCardCharger creditCardCharger;
    private final WalletRepository walletRepository;

    public TopUpWallet(@Qualifier("stripeCreditCardCharger") CreditCardCharger creditCardCharger, WalletRepository walletRepository) {
        this.creditCardCharger = creditCardCharger;
        this.walletRepository = walletRepository;
    }

    public void execute(Input input) {
        walletRepository.update(input.walletId, wallet -> wallet.topUp(input.amount))
                .orElseThrow(WalletNotFoundException::new);

        try {
            creditCardCharger.charge(new CreditCard(input.creditCardNumber), input.amount);
        } catch (Exception ex) {
            //Rollback
            walletRepository.update(input.walletId, wallet -> wallet.deduct(input.amount));
            throw ex;
        }
    }

    @Data
    @Builder
    public static class Input {
        @NonNull
        private Long walletId;

        @NonNull
        private String creditCardNumber;

        @NonNull
        private BigDecimal amount;
    }
}
