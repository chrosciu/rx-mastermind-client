package com.chrosciu.rxmastermindclient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class GameService {
    private final SessionService sessionService;

    private static final String SUCCESS_RESULT = "40";

    public Flux<String> getResults(Flux<String> samples) {
        Mono<Long> sessionId = sessionService.getSessionId();
        Flux<String> results = sessionId.flatMapMany(id -> samples
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(sample -> sessionService.getResult(id, sample))
                .takeUntil(SUCCESS_RESULT::equals)
                .doOnSubscribe(s -> System.out.println("Please enter samples: "))
                .doOnComplete(() -> System.out.println("Game finished"))
                .concatWith(sessionService.destroySession(id).then(Mono.empty()))
        );
        return results;
    }
}
