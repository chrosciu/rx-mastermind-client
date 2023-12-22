package eu.chrost.rxmastermindclient.runner;

import eu.chrost.rxmastermindclient.service.GameService;
import eu.chrost.rxmastermindclient.service.InputService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class GameRunner implements CommandLineRunner {
    private final InputService inputService;
    private final GameService gameService;

    @Override
    public void run(String... args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux<String> samples = inputService.getLines(System.in);
        gameService.getResults(samples)
                .doFinally(signalType -> countDownLatch.countDown())
                .subscribe(
                        System.out::println,
                        e -> log.warn("Error:", e)
                );
        countDownLatch.await();
    }

}
