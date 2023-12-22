package eu.chrost.rxmastermindclient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class GameService {
    private final SessionService sessionService;

    public Flux<String> getResults(Flux<String> samples) {
        Mono<Long> sessionId = sessionService.getSessionId();
        Flux<String> results = sessionId.flatMapMany(id -> samples
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(sample -> sessionService.getResult(id, sample))
        );
        return results;
    }
}
