package com.chrosciu.rxmastermindclient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.InputStream;

@RequiredArgsConstructor
public class InputService {
    private final InputStream inputStream;

    public Flux<String> getLines() {
        return null;
    }
}
