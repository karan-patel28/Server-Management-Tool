package com.ServerManagementTool.server;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ServerApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.addListeners((ApplicationListener<ContextRefreshedEvent>) event -> {
			System.out.println("Application has started");
		});
		app.run(args);
	}

}
