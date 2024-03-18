package eu.chrost.rxmastermindclient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Scanner;

@Service
@Slf4j
public class InputService {
    public Flux<String> getLines(InputStream inputStream) {
        return Flux.using(
                () -> new Scanner(inputStream),
                this::fromScanner,
                Scanner::close
        );
    }

    private Flux<String> fromScanner(Scanner scanner) {
        return Flux.<String>create(sink -> {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                sink.next(line);
            }
            sink.complete();
        });
    }
}
