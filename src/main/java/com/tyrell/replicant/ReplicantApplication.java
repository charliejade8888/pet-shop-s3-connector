package com.tyrell.replicant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReplicantApplication {
  // https://www.baeldung.com/java-record-vs-lombok
  public static void main(String[] args) {
    SpringApplication.run(ReplicantApplication.class, args);
  }
}
