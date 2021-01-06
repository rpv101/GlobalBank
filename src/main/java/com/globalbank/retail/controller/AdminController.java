package com.globalbank.retail.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globalbank.retail.repo.InternalUserRepository;
import com.globalbank.retail.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController()
@RequestMapping("/admin")
public class AdminController {
	InternalUserRepository repo;
	AdminService service;

	@Autowired
	public AdminController(InternalUserRepository repo, AdminService service) {
		this.service = service;
	}

	@PostMapping(path = "/logon")
	public ResponseEntity<String> logon(HttpServletRequest request) {
		return service.logon(request);
	}

	@PostMapping(path = "/employee/create")
	public ResponseEntity<String> createEmployee(HttpServletRequest request) {
		return service.create(request);
	}

	@PostMapping(path = "/employee/delete")
	public ResponseEntity<String> deleteEmployee(HttpServletRequest request) {
		return service.delete(request);

	}
}
