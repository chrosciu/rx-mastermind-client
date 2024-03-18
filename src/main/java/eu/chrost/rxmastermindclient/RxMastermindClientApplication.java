package eu.chrost.rxmastermindclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class RxMastermindClientApplication {

    public static void main(String[] args) {
        BlockHound.install();
        SpringApplication.run(RxMastermindClientApplication.class, args);
    }

}
