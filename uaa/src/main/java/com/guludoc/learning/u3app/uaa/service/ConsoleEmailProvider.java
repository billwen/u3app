package com.guludoc.learning.u3app.uaa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(prefix = "imooc.email-provider", name="name", havingValue="console")
public class ConsoleEmailProvider implements EmailService{
    @Override
    public void send(String email, String msg) {
        log.info("Sent email to {}, content {}", email, msg);
    }
}
