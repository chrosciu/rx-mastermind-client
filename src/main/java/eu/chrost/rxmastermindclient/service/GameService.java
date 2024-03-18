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
    private final InputService inputService;

    private static final String SUCCESS_RESULT = "40";

    public Flux<String> getResults() {
        return sessionService.getSessionId()
                .flatMapMany(
                        id -> inputService.getLines()
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(sample -> sessionService.getResult(id, sample))
                                .takeUntil(SUCCESS_RESULT::equals)
                                .doOnSubscribe(s -> System.out.println("Please enter samples: "))
                                .doOnComplete(() -> System.out.println("Game finished"))
                                .concatWith(sessionService.destroySession(id)
                                        .then(Mono.empty())
                                )
                );
    }
}
