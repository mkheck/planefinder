package com.thehecklers.planefinder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class PlanefinderApplication {
//	@Value("${planeip:192.168.1.249}")
//	private String planeIp;
//
//	@Bean
//	WebClient client() {
//		return WebClient.create("http://" + planeIp + "/ajax/aircraft");
//	}

	public static void main(String[] args) {
		//ReactorDebugAgent.init();
		SpringApplication.run(PlanefinderApplication.class, args);
	}

}
