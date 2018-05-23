package com.wyb.eurekaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages="com.wyb")
public class EurekaClient1Application {

	public static void main(String[] args) {
		System.out.println("=============> I am client1");
		SpringApplication.run(EurekaClient1Application.class, args);
	}
}
