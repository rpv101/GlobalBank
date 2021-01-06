package com.globalbank.retail;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class GlobalBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(GlobalBankApplication.class, args);
  }

}