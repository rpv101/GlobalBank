package com.globalbank.retail.repo;


import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.globalbank.retail.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {

}