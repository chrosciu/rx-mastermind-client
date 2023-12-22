package com.chrosciu.rxmastermindclient.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class SessionServiceTest {
    private WireMockServer wireMockServer;
    private WebClient webClient;
    private SessionService sessionService;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort().dynamicHttpsPort());
        wireMockServer.start();
        webClient = WebClient.builder().baseUrl(String.format("http://localhost:%d", wireMockServer.port())).build();
        sessionService = new SessionService(webClient);
    }

    @Test
    public void shouldGetSessionId() {
        //given
        long someSessionId = 3;
        wireMockServer.stubFor(post(urlEqualTo("/session"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(String.valueOf(someSessionId))));

        //when
        Mono<Long> sessionId = sessionService.getSessionId();

        //then
        StepVerifier.create(sessionId)
                .expectNext(someSessionId)
                .verifyComplete();
    }

    //TODO: Implement other tests
}
