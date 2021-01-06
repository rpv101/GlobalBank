package com.globalbank.retail.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.globalbank.retail.entity.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
	
	@Query("{'toCustomerId' : ?0 , 'date' : ?1}")
	List findCustomerStatementFrom(String custId, Date from);
}