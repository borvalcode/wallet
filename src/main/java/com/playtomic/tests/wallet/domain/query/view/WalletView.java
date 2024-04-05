package com.playtomic.tests.wallet.domain.query.view;

import com.playtomic.tests.wallet.domain.query.dto.WalletDetails;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletView {
  Optional<WalletDetails> get(long walletId);
}
