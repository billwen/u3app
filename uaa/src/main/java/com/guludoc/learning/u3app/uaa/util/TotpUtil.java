package com.guludoc.learning.u3app.uaa.util;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Component
public class TotpUtil {

    private static final long TIME_STEP = 60 * 5L;

    private static final int PASSWORD_LENGTH = 6;

    private KeyGenerator keyGenerator;

    private TimeBasedOneTimePasswordGenerator totp;

    {
        try {
            totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(TIME_STEP), PASSWORD_LENGTH);
            keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
            keyGenerator.init(512);
        } catch (NoSuchAlgorithmException e) {
            log.error("No such algorithm {}", e.getLocalizedMessage());
        }
    }

    public String createTotp(Key key, Instant time) throws InvalidKeyException {
        int password = totp.generateOneTimePassword(key, time);
        var format = "%0" + PASSWORD_LENGTH + "d";
        return String.format(format, password);
    }

    public boolean verifyTotp(Key key, String code) throws InvalidKeyException {
        Instant now = Instant.now();
        return code.equals(createTotp(key, now));
    }

    public Key generateKey() {
        return keyGenerator.generateKey();
    }

    public String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public String encodeKeyToString() {
        return encodeKeyToString(generateKey());
    }

    public Key decodeKeyToString(String encoded) {
        return new SecretKeySpec(Base64.getDecoder().decode(encoded), totp.getAlgorithm());
    }

    public Optional<String> createTotp(String strKey)  {
        try {
            return Optional.of(createTotp(decodeKeyToString(strKey), Instant.now()));
        } catch (InvalidKeyException e) {
            return Optional.empty();
        }
    }

    public long getTimeStepInSeconds() {
        return TIME_STEP;
    }
}
