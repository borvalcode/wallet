package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.domain.query.WalletDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    public void okTest() {
        long walletId = webTestClient.post()
                .uri("/wallets")
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDetails.class)
                .returnResult()
                .getResponseBody()
                .getId();

        webTestClient.get()
                .uri("/wallets/{walletId}", walletId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDetails.class)
                .isEqualTo(new WalletDetails(walletId, BigDecimal.ZERO));
    }

    @Test
    void walletNotFound() {
        webTestClient.get()
                .uri("/wallets/{walletId}", 123)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void invalidWalletId() {
        webTestClient.get()
                .uri("/wallets/{walletId}", "FOO")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
