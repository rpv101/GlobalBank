package com.globalbank.retail.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globalbank.retail.service.EmployeeService;

@RestController()
@RequestMapping("/employee")
public class EmployeeController {
	EmployeeService service;
	
	public EmployeeController(EmployeeService service) {
		this.service = service;
	}

	@PostMapping(path = "/logon")
	public ResponseEntity<String> logon(HttpServletRequest request) {
		return service.logon(request);
	}

	/* This endpoint will add the different types of account to system */
	@PostMapping (path = "/accounttypes/upsert")
	public ResponseEntity<String> createAccountTypes(HttpServletRequest request) {
		return service.createAccountTypes(request);
	}

	@PostMapping(path = "/customer/create")
	public ResponseEntity<String> createCustomer(HttpServletRequest request) {
		return service.createCustomer(request);
	}

	@PostMapping(path = "/customer/accounts/link")
	public ResponseEntity<String> linkCustomerToAccountType(HttpServletRequest request) {
		return service.linkCustomerToAccountType(request);
	}

	@PostMapping(path = "/customer/kyc/update")
	public String updateKYC(HttpServletRequest request) {
		//TODO
		return null;
	}

	@GetMapping (path = "/customer/get")
	public ResponseEntity<String> getCustomerDetails(HttpServletRequest request) {
		
		return service.getCustomerDetails(request);
	}

	@GetMapping(path = "/customer/delete")
	public ResponseEntity<String> deleteCustomer(HttpServletRequest request) {
		
		return service.deleteCustomer(request);
	}

	@GetMapping(path = "/customer/account/balance")
	public ResponseEntity<String> fetchAccountBalanceForCustomer(HttpServletRequest request) {
		
		return service.fetchAccountBalanceForCustomer(request);
	}

	@PostMapping(path = "/customer/account/transfer")
	public ResponseEntity<String> transferMoney(HttpServletRequest request) {
		
		return service.transferMoney(request);
	}

	@PostMapping(path = "/customer/account/print")
	public ResponseEntity<String> printAccountStatement(HttpServletRequest request) {
		
		return service.printAccountStatement(request);
	}

	@PostMapping(path = "/customer/account/interest")
	public String calculateAndUpdateBalanceWithInterest(HttpServletRequest request) {
		
		return service.calculateAndUpdateBalanceWithInterest(request);
	}


}
