package dev.lxqtpr.linda.t1task.clients;

import dev.lxqtpr.linda.t1task.dto.CreateCandidateDto;
import dev.lxqtpr.linda.t1task.dto.SetStatusDto;
import dev.lxqtpr.linda.t1task.encoders.Base64Encoder;
import dev.lxqtpr.linda.t1task.exceptions.RequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class T1ClientTest {

    private static final String status = "increased";
    private static final String testCandidateEmail = "forfollower13@gmail.com";

    @Autowired
    private T1Client t1Client;

    @Autowired
    private Base64Encoder base64Encoder;

    @Test
    void T1Client_GetRoles_ReturnsNonEmptyListHasSizeFive() {
        var roles = t1Client.getRoles().roles();
        assertThat(roles).isNotNull().hasSize(5);
    }

    @Test
    void T1Client_GetCode_ReturnsNonEmptyStringWithExistingEmail() {
        var code = t1Client.getCode(testCandidateEmail);

        assertThat(code)
                .isNotBlank();
    }
    @Test
    void T1Client_SignUp_ReturnsValidString() {
        var candidate = CreateCandidateDto.builder()
                .email(testCandidateEmail)
                .firstName("Daniil")
                .lastName("Gadelshin")
                .role("Разработчик Java")
                .build();
        var res = t1Client.sighUp(candidate);;
        assertThat(res)
                .isNotBlank()
                .isEqualTo("Данные внесены");
    }
    @Test
    void T1Client_SignUp_ThrowsRequestException() {
        var invalidCandidate = CreateCandidateDto.builder()
                .email(null)
                .build();
        assertThatThrownBy(() -> t1Client.sighUp(invalidCandidate))
                .isExactlyInstanceOf(RequestException.class);
    }

    @Test
    void T1Client_SetStatus_ReturnsValidString() {
        var code = t1Client.getCode(testCandidateEmail);
        var token = base64Encoder.encodeEmailAndCode(testCandidateEmail,code);
        var dto = new SetStatusDto(token, status);
        var res = t1Client.setStatus(dto);
        assertThat(res)
                .isNotBlank()
                .isEqualTo("Статус increased зафиксирован. Задание выполнено");
    }

    @Test
    void T1Client_SetStatus_ThrowsRequestException(){
        var invalidDto = new SetStatusDto(null, null);
        assertThatThrownBy(() -> t1Client.setStatus(invalidDto))
                .isExactlyInstanceOf(RequestException.class);
    }
}
