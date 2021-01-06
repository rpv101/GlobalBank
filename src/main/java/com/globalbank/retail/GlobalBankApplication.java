package com.globalbank.retail;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class GlobalBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(GlobalBankApplication.class, args);
  }

}