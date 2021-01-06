package com.globalbank.retail.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.globalbank.retail.entity.AccountType;

public interface AccountTypeRepository extends MongoRepository<AccountType, String> {

}