package com.playtomic.tests.wallet;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.playtomic.tests.wallet.api.TopupWalletRequest;
import com.playtomic.tests.wallet.domain.query.WalletDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@WireMockTest(httpPort = 9999)
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

    @Test
    void topUpOk() {
        stubFor(WireMock.post("/v1/stripe-simulator/charges")
                        .withHeader("Content-Type", containing("json"))
                        .withRequestBody(equalToJson("{\"credit_card\": \"4242 4242 4242 4242\", \"amount\": 5}"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"123\"}")));

        long walletId = webTestClient.post()
                .uri("/wallets")
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDetails.class)
                .returnResult()
                .getResponseBody()
                .getId();

        webTestClient.post()
                .uri("/wallets/{walletId}/top-up", walletId)
                .bodyValue(new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDetails.class)
                .isEqualTo(new WalletDetails(walletId, new BigDecimal(5)));
    }

    @Test
    void topUpErrorAmountTooSmall() {
        stubFor(WireMock.post("/v1/stripe-simulator/charges")
                .withHeader("Content-Type", containing("json"))
                .withRequestBody(equalToJson("{\"credit_card\": \"4242 4242 4242 4242\", \"amount\": 5}"))
                .willReturn(aResponse()
                        .withStatus(422)));

        long walletId = webTestClient.post()
                .uri("/wallets")
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDetails.class)
                .returnResult()
                .getResponseBody()
                .getId();

        webTestClient.post()
                .uri("/wallets/{walletId}/top-up", walletId)
                .bodyValue(new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        webTestClient.get()
                .uri("/wallets/{walletId}", walletId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDetails.class)
                .isEqualTo(new WalletDetails(walletId, BigDecimal.ZERO));
    }

    @Test
    void topUpErrorUnknown() {
        stubFor(WireMock.post("/v1/stripe-simulator/charges")
                .withHeader("Content-Type", containing("json"))
                .withRequestBody(equalToJson("{\"credit_card\": \"4242 4242 4242 4242\", \"amount\": 5}"))
                .willReturn(aResponse()
                        .withStatus(500)));

        long walletId = webTestClient.post()
                .uri("/wallets")
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDetails.class)
                .returnResult()
                .getResponseBody()
                .getId();

        webTestClient.post()
                .uri("/wallets/{walletId}/top-up", walletId)
                .bodyValue(new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        webTestClient.get()
                .uri("/wallets/{walletId}", walletId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDetails.class)
                .isEqualTo(new WalletDetails(walletId, BigDecimal.ZERO));
    }

    @Test
    void topUpUnknownWalletId() {
        webTestClient.post()
                .uri("/wallets/{walletId}/top-up", 123)
                .bodyValue(new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void topUpInvalidWalletId() {
        webTestClient.post()
                .uri("/wallets/{walletId}/top-up", "FOO")
                .bodyValue(new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(5)))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTopUpRequests")
    void topUpInvalidRequest(TopupWalletRequest request) {
        long walletId = webTestClient.post()
                .uri("/wallets")
                .exchange()
                .expectStatus().isOk()
                .expectBody(WalletDetails.class)
                .returnResult()
                .getResponseBody()
                .getId();

        webTestClient.post()
                .uri("/wallets/{walletId}/top-up", walletId)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private static List<TopupWalletRequest> provideInvalidTopUpRequests() {
        return Arrays.asList(new TopupWalletRequest("BAR", new BigDecimal(5)),
                new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(0)),
                new TopupWalletRequest("4242 4242 4242 4242", new BigDecimal(-1))
                );
    }
}
