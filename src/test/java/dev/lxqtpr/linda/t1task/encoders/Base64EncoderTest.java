package dev.lxqtpr.linda.t1task.encoders;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class Base64EncoderTest {

    @Autowired
    private Base64Encoder base64Encoder;

    @Test
    @DisplayName("Base64 encoded string should be equals init string")
    void Base64Encoder_EncodedValue_Equals_Decoded() {
        var email = "test_email";
        var code = "123";
        var resultString = email + ":" + code;
        var encodedString = base64Encoder.encodeEmailAndCode(email, code);
        assertThat(base64Encoder.decodeBase64(encodedString))
                .isNotBlank()
                .isEqualTo(resultString);
    }
}
