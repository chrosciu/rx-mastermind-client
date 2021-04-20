package com.chrosciu.rxmastermindclient.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("IMPLEMENT ME !");
        //TODO Implement game logic
    }

}
