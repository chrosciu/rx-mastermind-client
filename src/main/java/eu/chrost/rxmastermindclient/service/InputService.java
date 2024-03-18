package eu.chrost.rxmastermindclient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Scanner;

@RequiredArgsConstructor
@Slf4j
public class InputService {
    private final InputStream inputStream;

    public Flux<String> getLines() {
        return Flux.create(sink -> {
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                sink.next(line);
            }
            sink.complete();
        });
    }
}
