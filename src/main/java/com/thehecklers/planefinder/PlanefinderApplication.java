package com.thehecklers.planefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class PlanefinderApplication {
	@Bean
	WebClient client() {
		return WebClient.create("http://192.168.1.193/ajax/aircraft");
	}

	public static void main(String[] args) {
		ReactorDebugAgent.init();
		SpringApplication.run(PlanefinderApplication.class, args);
	}

}
