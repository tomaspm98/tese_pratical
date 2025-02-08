package com.tomas.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tomas")
public class Launcher {
	public static void main(String[] args) {
		SpringApplication.run(Launcher.class, args);
	}
}
