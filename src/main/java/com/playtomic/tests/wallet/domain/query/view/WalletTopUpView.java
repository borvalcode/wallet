package com.playtomic.tests.wallet.domain.query.view;

import com.playtomic.tests.wallet.domain.query.dto.WalletTopUpDetails;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public interface WalletTopUpView {
  Optional<WalletTopUpDetails> get(long walletId, long topUpId);
}
