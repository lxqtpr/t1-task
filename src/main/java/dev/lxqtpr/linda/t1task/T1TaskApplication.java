package dev.lxqtpr.linda.t1task;

import dev.lxqtpr.linda.t1task.clients.T1Client;
import dev.lxqtpr.linda.t1task.dto.CreateCandidateDto;
import dev.lxqtpr.linda.t1task.dto.SetStatusDto;
import dev.lxqtpr.linda.t1task.encoders.Base64Encoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class T1TaskApplication implements CommandLineRunner {

    private final T1Client taskClient;
    private final Base64Encoder encoder;
    private final String candidateEmail = "forfollower13@gmail.com";
    private static final String status = "increased";

    public static void main(String[] args) {
        SpringApplication.run(T1TaskApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var roles = taskClient.getRoles().roles();
        var javaIdx = roles.indexOf("Разработчик Java");

        var candidate = CreateCandidateDto.builder()
                .lastName("Gadelshin")
                .firstName("Daniil")
                .email("forfollower13@gmail.com")
                .role(roles.get(javaIdx))
                .build();
        var signUpRes = taskClient.sighUp(candidate);

        var code = taskClient.getCode(candidateEmail);
        var token = encoder.encodeEmailAndCode(candidateEmail, code);
        var dto = new SetStatusDto(token, status);
        var setStatusRes = taskClient.setStatus(dto);

        log.info(String.valueOf(roles));
        log.info(signUpRes);
        log.info(setStatusRes);
    }
}
