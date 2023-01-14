package com.guludoc.learning.u3app.uaa.service;

import com.guludoc.learning.u3app.uaa.config.Constants;
import com.guludoc.learning.u3app.uaa.domain.User;
import com.guludoc.learning.u3app.uaa.util.CryptoUtil;
import com.guludoc.learning.u3app.uaa.util.TotpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

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

    public String cacheUser(User user) {
        String mfaId = cryptoUtil.randomAlphanumeric(18);

        RMapCache<String, User> cache = redis.getMapCache(Constants.CACHE_MFA);

        if (!cache.containsKey(mfaId)) {
            cache.put(mfaId, user, totpUtil.getTimeStepInSeconds(), TimeUnit.SECONDS);
        }

        return mfaId;
    }

    public Optional<User> retrieveUser(String mfaId) {
        RMapCache<String, User> cache = redis.getMapCache(Constants.CACHE_MFA);

        if (cache.containsKey(mfaId)) {
            return Optional.of(cache.get(mfaId));
        }

        return Optional.empty();
    }

    public Optional<User> verifyTotp(String mfaId, String code) {
        RMapCache<String, User> cache = redis.getMapCache(Constants.CACHE_MFA);

        if (!cache.containsKey(mfaId) || cache.get(mfaId) == null) {
            return Optional.empty();
        }

        User cacheUser = cache.get(mfaId);
        try {
            boolean isValid = totpUtil.verifyTotp(totpUtil.decodeKeyToString(cacheUser.getMfaKey()), code);
            if (!isValid) {
                return Optional.empty();
            } else {
                cache.remove(mfaId);
                return Optional.of(cacheUser);
            }
        } catch (InvalidKeyException e) {
            return Optional.empty();
        }
    }
}
