package eu.chrost.rxmastermindclient.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;

@Service
public class InputService {
    public Flux<String> getLines(InputStream inputStream) {
        //TODO: Implement
        return null;
    }
}
