package eu.chrost.rxmastermindclient.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class InputServiceTest {
    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Test
    public void shouldBuildFluxFromGivenInputStream() {
        //given
        InputStream inputStream = buildMultilineInputStream(Arrays.asList("abc", "def", ""));
        InputService inputService = new InputService(inputStream);
        //when
        Flux<String> lines = inputService.getLines();
        //then
        StepVerifier.create(lines)
                .expectNext("abc")
                .expectNext("def")
                .expectNext("")
                .verifyComplete();
    }

    private InputStream buildMultilineInputStream(List<String> lines) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line: lines) {
            stringBuilder.append(line);
            stringBuilder.append(LINE_SEPARATOR);
        }
        return new ByteArrayInputStream(stringBuilder.toString().getBytes());
    }
}
