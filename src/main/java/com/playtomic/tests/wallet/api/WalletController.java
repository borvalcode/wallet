package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.dto.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {
    private final static Logger log = LoggerFactory.getLogger(WalletController.class);

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping("/wallets/{walletId}")
    Wallet getWallet(@PathVariable String walletId) {
        log.info("Retrieving wallet {}", walletId);

        return new Wallet(walletId);
    }
}
