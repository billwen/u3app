package com.guludoc.learning.u3app.uaa;

import com.guludoc.learning.u3app.uaa.service.EmbededRedisServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UaaApplication {

	@Autowired
	private EmbededRedisServer redisServer;
	public static void main(String[] args) {
		SpringApplication.run(UaaApplication.class, args);
	}

}
