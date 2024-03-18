package eu.chrost.rxmastermindclient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Scanner;

@Service
@Slf4j
public class InputService {
    public Flux<String> getLines(InputStream inputStream) {
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
