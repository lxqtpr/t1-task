package dev.lxqtpr.linda.t1task.encoders;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;

@Service
public class Base64Encoder{
    public String encodeEmailAndCode(String email, String code) {
        var combined = email + ":" + code;
        return Base64.getEncoder().encodeToString(combined.getBytes());
    }
    public String decodeBase64(String stringToDecode) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(stringToDecode);
        return new String(decodedBytes);
    }
}
