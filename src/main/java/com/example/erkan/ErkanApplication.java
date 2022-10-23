package com.example.erkan;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@SpringBootApplication
public class ErkanApplication {


	public static void main(String[] args) {
		SpringApplication.run(ErkanApplication.class, args);
	}

}
