package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		new SpringApplication(DemoApplication.class).run(args);
		printReady();
	}

	private static void printReady(){
		log.info("Ready to Rock!!");
	}

}
