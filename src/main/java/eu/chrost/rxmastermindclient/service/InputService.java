package eu.chrost.rxmastermindclient.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Scanner;

@Service
public class InputService {
    public Flux<String> getLines(InputStream inputStream) {
        return Flux.using(() -> new Scanner(inputStream),
                this::fromScanner,
                Scanner::close);
    }

    private Flux<String> fromScanner(Scanner scanner) {
        return Flux.create(sink -> {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                sink.next(line);
            }
            sink.complete();
        });
    }
}
