package com.project.Patika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpRequest;

@SpringBootApplication(scanBasePackages = "com.project.Patika")
public class PatikaApplication {

	public static void main(String[] args) throws Exception {

        SpringApplication.run(PatikaApplication.class, args);


	}


}
