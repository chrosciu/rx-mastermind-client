package eu.chrost.rxmastermindclient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
@RequiredArgsConstructor
public class GameService {
    private final SessionService sessionService;
    private final InputService inputService;

    private static final String SUCCESS_RESULT = "40";

    public Flux<String> getResults() {
        return Mono.just("Creating session...")
                .concatWith(sessionService.getSessionId().flatMapMany(id ->
                        Mono.just("Session created, please start entering samples:")
                                .concatWith(
                                        inputService.getLines(System.in)
                                                .subscribeOn(Schedulers.boundedElastic())
                                                .flatMap(sample ->
                                                        sessionService.getResult(id, sample)
                                                                .publishOn(Schedulers.boundedElastic())
                                                                .map(result ->
                                                                        Tuples.of(
                                                                                String.format("Result for sample %s: %s", sample, result),
                                                                                SUCCESS_RESULT.equals(result)
                                                                        )
                                                                )
                                                )
                                                .takeUntil(Tuple2::getT2)
                                                .flatMap(t ->
                                                        Flux.just(t.getT1())
                                                                .concatWith(t.getT2() ?
                                                                        Mono.just("Congratulations, you have guessed the code!") :
                                                                        Mono.empty())
                                                )
                                )
                                .concatWith(Mono.just("Game finished, destroying session..."))
                                .concatWith(
                                        sessionService.destroySession(id)
                                                .then(Mono.just("Session destroyed"))
                                )
                ));
    }
}
