package com.tyrell.replicant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestReplicantApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReplicantApplication::main).with(TestReplicantApplication.class).run(args);
	}

}
