package com.guludoc.learning.u3app.uaa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guludoc.learning.u3app.uaa.config.Constants;
import com.guludoc.learning.u3app.uaa.domain.User;
import com.guludoc.learning.u3app.uaa.util.CryptoUtil;
import com.guludoc.learning.u3app.uaa.util.TotpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidKeyException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserCacheService {

    private final RedissonClient redis;

    private final TotpUtil totpUtil;

    private final CryptoUtil cryptoUtil;

    private final ObjectMapper mapper;

    public String cacheUser(User user) throws AuthenticationException {
        String mfaId = cryptoUtil.randomAlphanumeric(18);

        RMapCache<String, String> cache = redis.getMapCache(Constants.CACHE_MFA);

        if (!cache.containsKey(mfaId)) {
            // Encode user first
            String encodedObj = null;
            try {
                encodedObj = mapper.writeValueAsString(user);
            } catch (JsonProcessingException e) {
                throw new BadCredentialsException("Error while encoding");
            }
            cache.put(mfaId, encodedObj, totpUtil.getTimeStepInSeconds(), TimeUnit.SECONDS);
        }

        return mfaId;
    }

    public Optional<User> retrieveUser(String mfaId) {
        RMapCache<String, String> cache = redis.getMapCache(Constants.CACHE_MFA);

        if (cache.containsKey(mfaId)) {
            String encodedObj = cache.get(mfaId);
            try {
                User user = mapper.readValue(encodedObj, User.class);
                return Optional.of(user);
            } catch (JsonProcessingException e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    public Optional<User> verifyTotp(String mfaId, String code) {
        RMapCache<String, String> cache = redis.getMapCache(Constants.CACHE_MFA);

        if (!cache.containsKey(mfaId) || cache.get(mfaId) == null) {
            return Optional.empty();
        }

        String encoded = cache.get(mfaId);

        try {
            User cachedUser = mapper.readValue(encoded, User.class);
            boolean isValid = totpUtil.verifyTotp(totpUtil.decodeKeyToString(cachedUser.getMfaKey()), code);
            if (!isValid) {
                return Optional.empty();
            } else {
                cache.remove(mfaId);
                return Optional.of(cachedUser);
            }
        } catch (InvalidKeyException e) {
            return Optional.empty();
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
