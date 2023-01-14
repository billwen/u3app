package com.guludoc.learning.u3app.uaa.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CryptoUtil {

    /**
     * 创建随机字母数字[0-9a-zA-Z]字符串
     * @param targetStringLength
     * @return
     */
    public String randomAlphanumeric(int targetStringLength) {
        int leftLimit = 48; // Number 0
        int rightLimit = 122; // Number 'z'

        Random random = new Random();

        return random.ints(leftLimit, rightLimit+1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
