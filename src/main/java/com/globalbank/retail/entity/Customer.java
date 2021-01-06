package com.globalbank.retail.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Customer {

	@Id
	public String id;

	public String firstName;
	public String lastName;
	public String email;
	public String mobile;
	public String accountType;
	public String balance;
}