package com.globalbank.retail.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class InternalUser {

	@Id
	public String userName;
	public String firstName;
	public String lastName;
	public String email;
	public String mobile;
	public String userType;
	public String session;

}