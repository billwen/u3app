package com.guludoc.learning.u3app.uaa.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.guludoc.learning.u3app.uaa.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(prefix = "mooc.message-provider", name = "name", havingValue = "ali")
public class AliSmsMessageService implements MessageService {

    private final AppProperties appProperties;

    private final IAcsClient client;

    @Override
    public void send(String mobile, String msg) {

        var request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(appProperties.getMessageProvider().getApiUrl());
        request.setSysAction("SendSms");
        request.setSysVersion("2017-05-25");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumber", mobile);
        request.putQueryParameter("SignName", "登录验证");
        request.putQueryParameter("TemplateCode", "SMS_1610048");
        request.putQueryParameter("TemplateParam", msg);

        try {
            var response = client.getCommonResponse(request);
            log.info("短信发送结果 {}", response.getData());
        } catch (ServerException e) {
            log.error("发送短信是发送服务端异常， {}", e.getLocalizedMessage());
        } catch (ClientException e) {
            log.error("发送短信时产生客户端异常, {}", e.getLocalizedMessage());
        }
    }
}
