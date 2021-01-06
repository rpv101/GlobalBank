package com.globalbank.retail.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalbank.retail.entity.InternalUser;
import com.globalbank.retail.repo.InternalUserRepository;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AdminService {
	InternalUserRepository repo;

	@Autowired
	public AdminService(InternalUserRepository repo) {
		this.repo = repo;
	}

	public ResponseEntity<String> create(HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();
		InternalUser requestPayload;
		request.getHeader("x-jwt-auth");

		try {
			requestPayload = mapper.readValue(request.getReader(), InternalUser.class);

		} catch (JsonMappingException e) {
			
			Log.error("Error while mapping the json string to entity");
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);

		} catch (JsonProcessingException e) {
			Log.error("Error while processing the json string");
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			Log.error("IO exception");
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		}
		repo.save(requestPayload);
		return new ResponseEntity<String>("Successfully created employee : " + requestPayload.id, HttpStatus.OK);

	}

	public ResponseEntity<String> delete(HttpServletRequest request) {
		ObjectMapper mapper = new ObjectMapper();
		InternalUser requestPayload;
		request.getHeader("x-jwt-auth");
		String employeeId = request.getHeader("entityId");

		repo.deleteById(employeeId);

		return new ResponseEntity<String>("Successfully deleted employee :" + employeeId, HttpStatus.OK);
	}

}
