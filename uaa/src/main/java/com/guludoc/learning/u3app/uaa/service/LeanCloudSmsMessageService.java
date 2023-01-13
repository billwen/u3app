package com.guludoc.learning.u3app.uaa.service;

import cn.leancloud.sms.AVSMS;
import cn.leancloud.sms.AVSMSOption;
import com.guludoc.learning.u3app.uaa.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(prefix = "imooc.message-provider", name="name", havingValue = "leancloud")
public class LeanCloudSmsMessageService implements MessageService{

    private final AppProperties appProperties;

    @Override
    public void send(String mobile, String msg) {
        var option = new AVSMSOption();
        option.setTtl(10);
        option.setApplicationName("慕课网实战Spring Security");
        option.setOperation("两步验证");
        option.setTemplateName("登录验证");
        option.setSignatureName("慕课网");
        option.setType(AVSMS.TYPE.TEXT_SMS);
        option.setEnvMap(Map.of("smsCode", msg));

        AVSMS.requestSMSCodeInBackground(mobile, option)
                .take(1)
                .subscribe(
                        (res) -> log.info("短信发送成功, {}", res),
                        (err) -> log.error("发送短信是产生服务端异常 {}", err.getLocalizedMessage())
                );
    }
}
