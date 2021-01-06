package com.globalbank.retail.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class AccountType {

	@Id
	public String accountType;
	
	public String description;

}