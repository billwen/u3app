package com.guludoc.learning.u3app.uaa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "imooc.message-provider", name = "name", havingValue = "console")
public class ConsoleMessageService implements MessageService{

    @Override
    public void send(String mobile, String msg) {
        log.info("Message to {}, content {}", mobile, msg);
    }
}
