package com.globalbank.retail.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalbank.retail.entity.InternalUser;
import com.globalbank.retail.entity.LoginRequest;
import com.globalbank.retail.helper.JwtTokenHelper;
import com.globalbank.retail.repo.InternalUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminService {
	static Logger log = Logger.getLogger(AdminService.class.getName());  
	
	InternalUserRepository repo;
	ObjectMapper mapper;
	JwtTokenHelper jwtTokenHelper;

	@Autowired
	public AdminService(InternalUserRepository repo, JwtTokenHelper jwtTokenHelper) {
		this.repo = repo;
		this.jwtTokenHelper = jwtTokenHelper;
		mapper = new ObjectMapper();
	}

	public ResponseEntity<String> create(HttpServletRequest request) {

		InternalUser requestPayload;

		try {
			requestPayload = mapper.readValue(request.getReader(), InternalUser.class);

		} catch (JsonMappingException e) {
			log.warning("Error while mapping the json string to entity");
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);

		} catch (JsonProcessingException e) {
			log.warning("Error while processing the json string");
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			log.warning("IO exception");
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		}
		repo.save(requestPayload);
		return new ResponseEntity<String>("Successfully created employee : " + requestPayload.userName, HttpStatus.OK);

	}

	public ResponseEntity<String> delete(HttpServletRequest request) {
		String employeeId = request.getHeader("entity_id");
		repo.deleteById(employeeId);

		return new ResponseEntity<String>("Successfully deleted employee :" + employeeId, HttpStatus.OK);
	}

	public ResponseEntity<String> logon(HttpServletRequest request) {
		LoginRequest requestPayload = null;
		try {
			requestPayload = mapper.readValue(request.getReader(), LoginRequest.class);
		} catch (IOException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		}

		InternalUser internalUser = repo.findById(requestPayload.customerId).get();

		if (internalUser.userType != null && internalUser.userType.equalsIgnoreCase("Admin")) {

			UserDetails userDetail = new User(internalUser.userName, "dummyPWD", new ArrayList<>());

			String jwt = jwtTokenHelper.generateToken(userDetail);
			internalUser.session = jwt;
			repo.save(internalUser);

			return new ResponseEntity<String>("Use this JWt for future  API calls for this Admin   :  " + jwt,
					HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("Error while processing payload or user doesnt have authority   :",
					HttpStatus.BAD_REQUEST);
		}
	}

}
