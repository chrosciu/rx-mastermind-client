package eu.chrost.rxmastermindclient.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
public class GameServiceTest {
    @Mock
    private InputService inputService;
    @Mock
    private SessionService sessionService;
    @InjectMocks
    private GameService gameService;

    @Test
    public void shouldGenerateResultsFromSessionAndKeyboardInput() {
        //given
        long someSessionId = 3;
        String[] lines = new String[]{"1234", "4567", "7890"};
        when(sessionService.getSessionId()).thenReturn(Mono.just(someSessionId));
        when(sessionService.destroySession(someSessionId)).thenReturn(Mono.empty());
        when(inputService.getLines(System.in)).thenReturn(Flux.just(lines));
        when(sessionService.getResult(someSessionId, lines[0])).thenReturn(Mono.just("20"));
        when(sessionService.getResult(someSessionId, lines[1])).thenReturn(Mono.just("40"));
        when(sessionService.getResult(someSessionId, lines[2])).thenReturn(Mono.just("11"));

        //when
        Flux<String> results = gameService.getResults();

        //then
        StepVerifier.create(results)
                .expectNext("Creating session...")
                .expectNext("Session created, please start entering samples:")
                .expectNext("Result for sample 1234: 20")
                .expectNext("Result for sample 4567: 40")
                .expectNext("Congratulations, you have guessed the code!")
                .expectNext("Game finished, destroying session...")
                .expectNext("Session destroyed")
                .verifyComplete();
    }

    @Test
    public void shouldGenerateResultsFromSessionAndKeyboardInputUsingTestPublisher() {
        //given
        long someSessionId = 3;
        String[] lines = new String[]{"1234", "4567"};
        when(sessionService.getSessionId()).thenReturn(Mono.just(someSessionId));
        when(sessionService.destroySession(someSessionId)).thenReturn(Mono.empty());
        TestPublisher<String> publisher = TestPublisher.createCold();
        when(inputService.getLines(System.in)).thenReturn(publisher.flux());
        when(sessionService.getResult(someSessionId, lines[0])).thenReturn(Mono.just("20"));

        //when
        Flux<String> results = gameService.getResults();
        publisher.next(lines[0]);

        //then
        verify(sessionService, times(0)).destroySession(someSessionId);
        StepVerifier.create(results)
                .expectNext("Creating session...")
                .expectNext("Session created, please start entering samples:")
                .expectNext("Result for sample 1234: 20")
                .expectNoEvent(Duration.of(1, ChronoUnit.SECONDS))
                .thenCancel()
                .verify();
        verify(sessionService, times(1)).destroySession(someSessionId);
    }
}
