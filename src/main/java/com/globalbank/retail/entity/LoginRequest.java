package com.globalbank.retail.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class LoginRequest {

	@Id
	public String customerId;
	
	public String password;

}