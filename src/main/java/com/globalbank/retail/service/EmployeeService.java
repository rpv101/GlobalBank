package com.globalbank.retail.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalbank.retail.entity.AccountType;
import com.globalbank.retail.entity.Customer;
import com.globalbank.retail.entity.InternalUser;
import com.globalbank.retail.entity.LoginRequest;
import com.globalbank.retail.entity.Statement;
import com.globalbank.retail.entity.Transaction;
import com.globalbank.retail.helper.JwtTokenHelper;
import com.globalbank.retail.repo.AccountTypeRepository;
import com.globalbank.retail.repo.CustomerRepository;
import com.globalbank.retail.repo.InternalUserRepository;
import com.globalbank.retail.repo.TransactionRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeService {
	CustomerRepository custRepo;
	InternalUserRepository internalRepo;
	AccountTypeRepository accRepo;
	TransactionRepository transRepo;
	JwtTokenHelper jwtTokenHelper;
	ObjectMapper mapper;

	public EmployeeService(CustomerRepository custRepo, InternalUserRepository internalRepo,
			TransactionRepository transRepo, AccountTypeRepository accRepo, JwtTokenHelper jwtTokenHelper) {
		this.custRepo = custRepo;
		this.accRepo = accRepo;
		this.transRepo = transRepo;
		this.jwtTokenHelper = jwtTokenHelper;
		this.internalRepo = internalRepo;
		mapper = new ObjectMapper();
	}

	public ResponseEntity<String> createAccountTypes(HttpServletRequest request) {
		AccountType[] requestPayload;
		request.getHeader("x-jwt-auth");

		try {
			requestPayload = mapper.readValue(request.getReader(), AccountType[].class);

		} catch (JsonMappingException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);

		} catch (JsonProcessingException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		}
		for (AccountType accType : requestPayload) {
			accRepo.save(accType);
		}
		return new ResponseEntity<String>("Successfully upserted accounttype : ", HttpStatus.OK);
	}

	public ResponseEntity<String> createCustomer(HttpServletRequest request) {
		Customer requestPayload;

		try {
			requestPayload = mapper.readValue(request.getReader(), Customer.class);

		} catch (JsonMappingException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);

		} catch (JsonProcessingException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		}

		custRepo.save(requestPayload);
		return new ResponseEntity<String>("Successfully created customer : " + requestPayload.id, HttpStatus.OK);
	}

	public ResponseEntity<String> linkCustomerToAccountType(HttpServletRequest request) {
		Customer requestPayload;
		try {
			requestPayload = mapper.readValue(request.getReader(), Customer.class);

		} catch (JsonMappingException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);

		} catch (JsonProcessingException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		}
		String custId = requestPayload.id;
		String accountType = requestPayload.accountType;
		if (!StringUtils.isEmpty(accountType)) {
			Optional<AccountType> accTypeEntity = accRepo.findById(accountType);
			if (!accTypeEntity.isPresent()) {
				return new ResponseEntity<String>(
						"Error while processing request . AccountType not found in system :" + accountType,
						HttpStatus.BAD_REQUEST);
			}
			;
		}
		if (!StringUtils.isEmpty(custId)) {
			Optional<Customer> custEntity = custRepo.findById(custId);
			if (custEntity.isPresent()) {
				Customer entity = custEntity.get();
				entity.accountType = accountType;
				custRepo.save(entity);
			}
			;
		} else {
			return new ResponseEntity<String>(
					"Error while processing request . Customer not found in system :" + custId, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Successfully upserted accounttype : ", HttpStatus.OK);
	}

	public ResponseEntity<String> getCustomerDetails(HttpServletRequest request) {
		String custId = request.getHeader("entity_id");
		Optional<Customer> cust = custRepo.findById(custId);
		if (cust.isEmpty()) {
			return new ResponseEntity<String>(
					"Error while processing request . Customer not found in system :" + custId, HttpStatus.BAD_REQUEST);
		} else {
			Customer customer = cust.get();
			String customerDetails = "Firstname : ".concat(customer.firstName).concat("   Lastname :")
					.concat(customer.lastName).concat("   Balance :").concat(customer.balance);
			return new ResponseEntity<String>(customerDetails, HttpStatus.OK);
		}

	}

	public ResponseEntity<String> deleteCustomer(HttpServletRequest request) {
		request.getHeader("x-jwt-auth");
		String custId = request.getHeader("entity_id");
		Optional<Customer> cust = custRepo.findById(custId);
		if (cust.isEmpty()) {
			return new ResponseEntity<String>(
					"Error while processing request . Customer not found in system :" + custId, HttpStatus.BAD_REQUEST);
		} else {
			custRepo.deleteById(custId);
			return new ResponseEntity<String>("Succesfully deleted customer :" + cust, HttpStatus.OK);
		}
	}

	public ResponseEntity<String> fetchAccountBalanceForCustomer(HttpServletRequest request) {
		String custId = request.getHeader("entity_id");
		Optional<Customer> cust = custRepo.findById(custId);
		if (cust.isEmpty()) {
			return new ResponseEntity<String>(
					"Error while processing request . Customer not found in system :" + custId, HttpStatus.BAD_REQUEST);
		} else {
			Customer customer = cust.get();
			return new ResponseEntity<String>("Balance in customer account is " + customer.balance, HttpStatus.OK);
		}
	}

	public ResponseEntity<String> transferMoney(HttpServletRequest request) {
		Transaction requestPayload;
		try {
			requestPayload = mapper.readValue(request.getReader(), Transaction.class);

		} catch (JsonMappingException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);

		} catch (JsonProcessingException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		}

		if (StringUtils.isEmpty(requestPayload.toCustomerId) || StringUtils.isEmpty(requestPayload.fromCustomerId)
				|| StringUtils.isEmpty(requestPayload.amount)) {
			return new ResponseEntity<String>("Bad request payload", HttpStatus.BAD_REQUEST);
		}

		else {
			return updateTransactions(requestPayload);
		}

	}

	private ResponseEntity<String> updateTransactions(Transaction requestPayload) {
		try {
			Customer currentSourceEntity = custRepo.findById(requestPayload.fromCustomerId).get();
			Customer currentDestinationEntity = custRepo.findById(requestPayload.toCustomerId).get();

			if (null != currentSourceEntity && null != currentDestinationEntity) {
				updateTransactionTable(requestPayload);
				updateCustomerTable(currentSourceEntity, currentDestinationEntity);
			} else {
				return new ResponseEntity<String>("Source or destination account not found", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			rollbackTransactions();
		}
		return new ResponseEntity<String>("Transaction succesfull", HttpStatus.OK);
	}

	private void rollbackTransactions() {
		// TODO Need to write proper rollback strategy

	}

	private void updateTransactionTable(Transaction requestPayload) {
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		requestPayload.date = date;
		requestPayload.transactionType = "Debit";
		transRepo.save(requestPayload);
	}

	private void updateCustomerTable(Customer currentSourceEntity, Customer currentDestinationEntity) {
		// TODO update the balance of the source and destination account
	}

	public ResponseEntity<String> printAccountStatement(HttpServletRequest request) {
		Statement requestPayload;
		try {
			requestPayload = mapper.readValue(request.getReader(), Statement.class);
			Date date = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(requestPayload.fromDate);
			List<Transaction> transactions = transRepo.findCustomerStatementFrom(requestPayload.customerId, date);

			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(requestPayload.customerId.concat(".pdf")));

			document.open();
			Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

			String transactionString = "";
			for (Transaction transaction : transactions) {
				transactionString = transactionString.concat("  From account:").concat(transaction.fromCustomerId)
						.concat(" To account:")
						.concat(transaction.toCustomerId.concat("  Amount :").concat(transaction.amount).concat("\n"));
			}

			Chunk chunk = new Chunk(transactionString, font);

			document.add(chunk);
			document.close();

		} catch (JsonMappingException e) {
			return new ResponseEntity<String>("Error while mapping JSON payload", HttpStatus.BAD_REQUEST);

		} catch (JsonProcessingException e) {
			return new ResponseEntity<String>("Error while processing JSON payload", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		} catch (ParseException e) {
			return new ResponseEntity<String>("Error while parsing date", HttpStatus.BAD_REQUEST);
		} catch (DocumentException e) {
			return new ResponseEntity<String>("Error while creating PDF document", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Successfully generated PDF", HttpStatus.OK);
	}

	public String calculateAndUpdateBalanceWithInterest(HttpServletRequest request) {
		return null;
	}

	public ResponseEntity<String> logon(HttpServletRequest request) {
		LoginRequest requestPayload = null;
		InternalUser internalUser = null;
		try {
			requestPayload = mapper.readValue(request.getReader(), LoginRequest.class);
		} catch (IOException e) {
			return new ResponseEntity<String>("Error while processing payload", HttpStatus.BAD_REQUEST);
		}

		Optional<InternalUser> internalUserOptional = internalRepo.findById(requestPayload.customerId);

		if (internalUserOptional.isPresent()) {
			internalUser = internalUserOptional.get();
		} else {
			return new ResponseEntity<String>("Employee not found !", HttpStatus.BAD_REQUEST);
		}

		if (internalUser.userType != null && internalUser.userType.equalsIgnoreCase("Employee")) {

			UserDetails userDetail = new User(internalUser.userName, "dummyPWD", new ArrayList<>());

			String jwt = jwtTokenHelper.generateToken(userDetail);
			internalUser.session = jwt;
			internalRepo.save(internalUser);

			return new ResponseEntity<String>("Use this JWt for future  API calls for this Employee  :  " + jwt,
					HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("Error while processing payload or user doesnt have authority",
					HttpStatus.BAD_REQUEST);
		}
	}

}
