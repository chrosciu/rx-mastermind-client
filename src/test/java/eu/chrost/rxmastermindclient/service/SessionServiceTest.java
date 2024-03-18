package eu.chrost.rxmastermindclient.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class SessionServiceTest {
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

    @Test
    public void shouldReturnGuessResultForGivenSampleAndSessionWithGivenId() {
        //given
        long someSessionId = 3;
        String someSample = "1234";
        String someGuess = "23";
        wireMockServer.stubFor(put(urlEqualTo("/session/3/1234"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(someGuess)));

        //when
        Mono<String> guess = sessionService.getResult(someSessionId, someSample);

        //then
        StepVerifier.create(guess)
                .expectNext(someGuess)
                .verifyComplete();
    }

    @Test
    public void shouldReturnErrorMessageIfGuessEndpointReturnsError() {
        //given
        long someSessionId = 3;
        String someSample = "1234";
        wireMockServer.stubFor(put(urlEqualTo("/session/3/1234"))
                .willReturn(aResponse()
                        .withStatus(400)));

        //when
        Mono<String> guess = sessionService.getResult(someSessionId, someSample);

        //then
        StepVerifier.create(guess)
                .expectNext("Bad input")
                .verifyComplete();
    }

    @Test
    public void shouldDestroySessionWithGivenId() {
        //given
        long someSessionId = 3;
        wireMockServer.stubFor(delete(urlEqualTo("/session/3"))
                .willReturn(aResponse()
                        .withStatus(200)));

        //when
        Mono<Void> result = sessionService.destroySession(someSessionId);

        //then
        StepVerifier.create(result)
                .verifyComplete();
    }
}
