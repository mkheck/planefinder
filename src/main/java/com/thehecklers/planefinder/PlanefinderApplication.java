package com.thehecklers.planefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;
//import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class PlanefinderApplication {
	public static void main(String[] args) {
		//ReactorDebugAgent.init();
		Hooks.onErrorDropped(e -> System.out.println("\n<< Client disconnected >>\n"));
		SpringApplication.run(PlanefinderApplication.class, args);
	}

}
